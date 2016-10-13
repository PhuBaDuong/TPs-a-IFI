package program;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import choco.Choco;
import choco.Options;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.solver.Solver;

public class TimeTablingChoco {
	CPModel cpModel;
	CPSolver cpSolver;
	private IntegerVariable[] xd; // xd[c] = D
	private IntegerVariable[] xs; // xs[c] = p
	private List<Course> courses; // list of courses
	private List<Professor> professors;// list of professors
	int D = 6; // the day of week 2-6
	int p = 6; // the slot every day 1-6
	int[] c; // array of course
	int[] d; // duration of course

	IntegerExpressionVariable[] Load;

	public boolean isIntersection(List<ClassName> course1,
			List<ClassName> course2) {
		List<ClassName> intersection = course1;
		intersection.retainAll(course2);
		if (intersection.size() == 0 || intersection == null)
			return false;
		return true;
	}

	public boolean twoClassesListsIntersecting(List<ClassName> list1,
			List<ClassName> list2) {
		if (list1 == null || list2 == null)
			return false;
		for (int i = 0; i < list1.size(); i++) {
			for (int j = 0; j < list2.size(); j++) {
				if (list1.get(i).getName().equals(list2.get(j).getName()))
					return true;
			}
		}
		return false;
	}

	public void model() {
		cpModel = new CPModel();
		// cpSolver = new CPSolver();
		professors = new ArrayList<Professor>();
		courses = new ArrayList<Course>();
		ReadAndPrintXMLFile.readFile("data/timetabling.xml", professors,
				courses);

		xd = new IntegerVariable[courses.size()];
		for (int i = 0; i < courses.size(); i++) {
			xd[i] = Choco.makeIntVar("xd" + i, 2, D, Options.V_ENUM);
		}
		xs = new IntegerVariable[courses.size()];
		for (int i = 0; i < courses.size(); i++) {
			xs[i] = Choco.makeIntVar("xs" + i, 1, p, Options.V_ENUM);
		}

		IntegerVariable b = Choco.makeIntVar("z", 0, 1);
		d = new int[courses.size()];

		// Contrainte 1: xd(c), xs(c) + i !∈ B(p(c)), ∀c ∈ C , i ∈ {0, 1, . .
		// .,d(c) − 1}
		for (int i = 0; i < courses.size(); i++) {
			int professorIdCourseI = courses.get(i).getInstructor();
			for (int j = 0; j < professors.size(); j++) {
				if (professors.get(j).getId() == professorIdCourseI) {
					List<Busy> busysOfProfJ = professors.get(j).getBusyList();
					for (int k = 0; k < busysOfProfJ.size(); k++) {
						int dayBusy = busysOfProfJ.get(k).getDay();
						int slotBusy = busysOfProfJ.get(k).getSlot();
						cpModel.addConstraint(Choco.implies(Choco.eq(Choco.minus(xd[i], dayBusy), 0), Choco.or(
								Choco.leq(Choco.plus(xs[i], d[i]), slotBusy),
								Choco.leq(slotBusy + 1, xs[i]))));
					}
				}
			}
		}
		// First constraint
		// xs(c) + d(c) ≤ p + 1, ∀c ∈ C
		for (int i = 0; i < courses.size(); i++) {
			d[i] = courses.get(i).getNumberOfSlots();// get duration of course i
														// - 1
		}

		for (int i = 0; i < courses.size(); i++) {
			IntegerExpressionVariable temp = Choco.plus(xs[i], d[i]);
			cpModel.addConstraint(Choco.leq(temp, p + 1));
		}

		// Second constraint
		// p(c1 ) = p(c2 ) ⇒ NotOvertp(c1 , c2 ), ∀ c1 = c2 ∈ C
		for (int i = 0; i < courses.size() - 1; i++) {
			for (int j = i + 1; j < courses.size(); j++) {
				if (courses.get(i).getInstructor() == courses.get(j)
						.getInstructor()) {
					cpModel.addConstraint(Choco.implies(Choco.eq(xd[i], xd[j]),
							Choco.or(Choco.leq(Choco.plus(xs[i], d[i]), xs[j]),
									Choco.leq(Choco.plus(xs[j], d[j]), xs[i]))));
				}
			}
		}

		// //Third constraint
		// // classes(c1 ) ∩ classes(c2 ) != ∅ ⇒ NotOverLap(c1 , c2 ), ∀c1 = c2
		// ∈ C

		for (int i = 0; i < courses.size() - 1; i++) {
			List<ClassName> firstClass = courses.get(i).getClassNames();
			for (int j = i + 1; j < courses.size(); j++) {
				List<ClassName> secondClass = courses.get(j).getClassNames();
				if (twoClassesListsIntersecting(firstClass, secondClass)) {
					cpModel.addConstraint(Choco.implies(Choco.eq(xd[i], xd[j]),
							Choco.or(Choco.leq(Choco.plus(xs[i], courses.get(i)
									.getNumberOfSlots()), xs[j]), Choco.leq(
									Choco.plus(xs[j], courses.get(j)
											.getNumberOfSlots()), xs[i]))));
				}
			}
		}
	}

	void search() throws Exception {
		cpSolver = new CPSolver();
		cpSolver.read(cpModel);
		System.out.println(cpSolver.solve());
		if (cpSolver.solve()) {
			printResult();
			printSolution();
		}
	}

	public void printSolution() {
		System.out.println("Result ");
		System.out.println("xd");
		for (int i = 0; i < courses.size(); i++)
			System.out.printf(cpSolver.getVar(xd[i]).getVal() + " ");
		System.out.println();
		System.out.println("xs");
		for (int i = 0; i < courses.size(); i++)
			System.out.printf(cpSolver.getVar(xs[i]).getVal() + " ");
	}

	public void printResult() throws Exception {
		PrintWriter writer = new PrintWriter("data/resultChoco.html", "UTF-8");
		writer.println("<html> <header><meta charset=\"UTF-8\"><style> table, th, td {"
				+ "border: 1px solid black;border-collapse: collapse; }"
				+ "td {	width: 1200px;}"
				+ "tr {text-align:center;}"
				+ "div .day {width:200px;float: left;} "
				+ "div .slot {width: 32.5px;height: 45px;float: left;}"
				+ "div .slot {background-color: #b0c4de;border: 1px solid black;}"
				+ "div .slot .yes .no {width: 32.5px;height: 150px;float: left;}"
				+ "div .yes {background-color: orange !important;height: 150px;width: 32.5px;border: 1px solid black;}"
				+ "div .no {background-color: white !important;height: 150px;}"
				+ "</style></header><body><table><tr><td>Thời Khóa Biểu</td></tr>"
				+ "<tr><td><div><div>Lớp</div>"
				+ "</td>"
				+ "<td>"
				+ "<div class='day'>"
				+ "<div>Thứ 2</div>"
				+ "<div class=\"slot\">tiết 1</div>"
				+ "<div class=\"slot\">tiết 2</div>"
				+ "<div class=\"slot\">tiết 3</div>"
				+ "<div class=\"slot\">tiết 4 </div>"
				+ "<div class=\"slot\">tiết 5</div>"
				+ "<div class=\"slot\">tiết 6</div>"
				+ "</div>"
				+ "</td>"
				+ "<td>"
				+ "<div class=\"day\"><div>Thứ 3</div>"
				+ "<div class=\"slot\">tiết 1</div>"
				+ "<div class=\"slot\">tiết 2</div>"
				+ "<div class=\"slot\">tiết 3</div>"
				+ "<div class=\"slot\">tiết 4 </div>"
				+ "<div class=\"slot\">tiết 5</div>"
				+ "<div class=\"slot\">tiết 6</div>"
				+ "</div>"
				+ "</td>"
				+ "<td>"
				+ "<div class=\"day\">"
				+ "<div>Thu 4</div>"
				+ "<div class=\"slot\">tiết 1</div>"
				+ "<div class=\"slot\">tiết 2</div>"
				+ "<div class=\"slot\">tiết 3</div>"
				+ "<div class=\"slot\">tiết 4 </div>"
				+ "<div class=\"slot\">tiết 5</div>"
				+ "<div class=\"slot\">tiết 6</div>"
				+ "</div>"
				+ "</td>"
				+ "<td>"
				+ "<div class=\"day\">"
				+ "<div>Thu 5</div>"
				+ "<div class=\"slot\">tiết 1</div>"
				+ "<div class=\"slot\">tiết 2</div>"
				+ "<div class=\"slot\">tiết 3</div>"
				+ "<div class=\"slot\">tiết 4 </div>"
				+ "<div class=\"slot\">tiết 5</div>"
				+ "<div class=\"slot\">tiết 6</div>"
				+ "</div>"
				+ "</td>"
				+ "<td>"
				+ "<div class=\"day\">"
				+ "<div>Thu 6</div>"
				+ "<div class=\"slot\">tiết 1</div>"
				+ "<div class=\"slot\">tiết 2</div>"
				+ "<div class=\"slot\">tiết 3</div>"
				+ "<div class=\"slot\">tiết 4 </div>"
				+ "<div class=\"slot\">tiết 5</div>"
				+ "<div class=\"slot\">tiết 6</div>"
				+ "</div>"
				+ "</td>"
				+ "</tr>");

		List<ClassName> allClasses = new ArrayList<ClassName>();
		for (int i = 0; i < courses.size(); i++) {
			for (int j = 0; j < courses.get(i).getClassNames().size(); j++) {
				ClassName tmp = courses.get(i).getClassNames().get(j);
				int k = 0;
				for (k = 0; k < allClasses.size();) {
					if (!allClasses.get(k).equals(tmp)) {
						k++;
					} else
						break;
				}
				if (k == allClasses.size()) {
					allClasses.add(tmp);
				}

			}
		}

		for (int i = 0; i < allClasses.size(); i++) {
			writer.println("<tr>" + "<td>" + "<div>"
					+ allClasses.get(i).getName() + "</div>" + "</td>");
			for (int d = 2; d <= D; d++) {
				writer.println("<td>" + "<div class=\"day\">");
				for (int s = 1; s <= p;) {
					int m = s;
					for (int j = 0; j < courses.size(); j++) {
						if (cpSolver.getVar(xd[j]).getVal() == d
								&& cpSolver.getVar(xs[j]).getVal() == s) {
							for (int c = 0; c < courses.get(j).getClassNames()
									.size(); c++) {
								ClassName tmp = courses.get(j).getClassNames()
										.get(c);
								if (allClasses.get(i).equals(tmp)) {
									writer.println("<div class=\"slot yes\" style=\"width :"
											+ 34.5
											* courses.get(j).getNumberOfSlots()
											+ ";\">"
											+ courses.get(j).getName()
											+ "<br>Prof : "
											+ courses.get(j).getInstructor()
											+ "<br>Dur :"
											+ courses.get(j).getNumberOfSlots()
											+ "</div>");
									s = s + courses.get(j).getNumberOfSlots();
								}
							}
						}
					}
					if (s == m) {
						writer.println("<div class=\"slot no\"></div>");
						s++;
					}
				}
				writer.println("</div>" + "</td>");
			}
			writer.println("</tr>");
		}
		writer.println("</table></body></html>");
		writer.close();
	}

	public static void main(String[] args) throws Exception {
		TimeTablingChoco timeTabling = new TimeTablingChoco();

		timeTabling.model();
		timeTabling.search();
	}
}