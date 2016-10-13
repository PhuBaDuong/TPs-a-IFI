package program;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotOverLap;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.TabuSearch;

public class TimeTablingOpenLS {
		private List<Professor> professors;
		private List<Course> courses;
		private VarIntLS[] xd; // xd[c] = D
		private VarIntLS[] xs; // xs[c] = p
		int D = 6; // the day of week 2-6
		int p = 6; // the slot every day 1-6

		Random r = new Random();

		LocalSearchManager ls;
		ConstraintSystem S;

		public void stateModel() {
			ls = new LocalSearchManager();
			S = new ConstraintSystem(ls);
			professors = new ArrayList<Professor>();
			courses = new ArrayList<Course>();
			ReadAndPrintXMLFile.readFile("data/timetabling.xml", professors,
					courses);

			xd = new VarIntLS[courses.size()];
			xs = new VarIntLS[courses.size()];
			for (int i = 0; i < courses.size(); i++) {
				xd[i] = new VarIntLS(ls, 2, D);
				xs[i] = new VarIntLS(ls, 1, p);
			}

			// Contrainte 1: xd(c), xs(c) + i !∈ B(p(c)), ∀c ∈ C , i ∈ {0, 1, . .
			// .,d(c) − 1}
			VarIntLS xdTmp = new VarIntLS(ls, 2, 6);
			VarIntLS xsTmp = new VarIntLS(ls, 1, 6);

			for (int i = 0; i < courses.size(); i++) {
				int professorIdCourseI = courses.get(i).getInstructor();
				for (int j = 0; j < professors.size(); j++) {
					if (professors.get(j).getId() == professorIdCourseI) {
						List<Busy> busysOfProfJ = professors.get(j).getBusyList();
						for (int k = 0; k < busysOfProfJ.size(); k++) {
							xdTmp = new VarIntLS(ls, 2, 6);
							xsTmp = new VarIntLS(ls, 1, 6);
							xdTmp.setValue(busysOfProfJ.get(k).getDay());
							xsTmp.setValue(busysOfProfJ.get(k).getSlot());
							S.post(new Implicate(new IsEqual(xd[i], xdTmp), new NotOverLap(xs[i], courses
									.get(i).getNumberOfSlots(), xsTmp, 1)));
						}
					}
				}
			}

			// // Contrainte 2: xs(c) + d(c) ≤ p + 1, ∀c ∈ C
			for (int i = 0; i < xs.length; i++) {
				S.post(new LessOrEqual(xs[i], p + 1
						- courses.get(i).getNumberOfSlots()));
			}

			// Contrainte 3: p(c1 ) = p(c2 ) ⇒ NotOverLap(c1 , c2 ), ∀ c1 = c2 ∈ C
			for (int i = 0; i < courses.size() - 1; i++) {
				for (int j = i + 1; j < courses.size(); j++) {
					if (courses.get(i).getInstructor() == courses.get(j)
							.getInstructor()) {
						S.post(new Implicate(new IsEqual(xd[i], xd[j]), new NotOverLap(xs[i], courses.get(i)
								.getNumberOfSlots(), xs[j], courses.get(j)
								.getNumberOfSlots())));
					}
				}
			}

			// Contrainte 4: classes(c1 ) ∩ classes(c2 ) != ∅ ⇒ NotOverLap(c1 , c2),
			// ∀c1 = c2 ∈ C
			for (int i = 0; i < courses.size() - 1; i++) {
				List<ClassName> classesCourseI = courses.get(i).getClassNames();
				for (int j = i + 1; j < courses.size(); j++) {
					List<ClassName> classesCourseJ = courses.get(j).getClassNames();
					if (twoClassesListsIntersecting(classesCourseI, classesCourseJ)) {
						S.post(new Implicate(new IsEqual(xd[i], xd[j]), new NotOverLap(xs[i], courses.get(i)
								.getNumberOfSlots(), xs[j], courses.get(j)
								.getNumberOfSlots())));
					}
				}
			}

			ls.close();
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

		public void search() throws Exception {
			TabuSearch tb = new TabuSearch();
			long time = System.currentTimeMillis();
			tb.search(S, 30, 5, 10000, 100);
			time = (System.currentTimeMillis() - time);
			System.out.println("Number of violations " + S.violations());
			System.out.println("Time : " + time);
			for (int i = 0; i < xd.length; i++)
				System.out.printf(i % 10 + " ");
			System.out.println("\nxd :");
			for (int i = 0; i < xd.length; i++)
				System.out.printf(xd[i].getValue() + " ");
			System.out.println("\nxs :");
			for (int i = 0; i < xs.length; i++)
				System.out.printf(xs[i].getValue() + " ");
			if (S.violations() == 0) {
				printResult();
			}

		}

		public void printResult() throws Exception {
			PrintWriter writer = new PrintWriter("data/resultOpenLS.html", "UTF-8");
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
							if (xd[j].getValue() == d && xs[j].getValue() == s) {
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
												+ "<br>Prof: "
												+ courses.get(j).getInstructor() + "<br>Dur:" + courses.get(j).getNumberOfSlots()
												+ "</div>");
										s = s + courses.get(j).getNumberOfSlots();
									}
								}
							}
						}
						if(s == m){
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
			TimeTablingOpenLS timeTabling = new TimeTablingOpenLS();
			timeTabling.stateModel();
			timeTabling.search();
		}
}
