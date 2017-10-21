package Lab4;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * @author asus
 *
 */
public final class PairProgramming {
	/**
	 * aaa.
	 */
	private PairProgramming() {
	}
	/**
	 * @param args o
	 */
	public static void main(final String[] args) {
		final int createGRAPH = 1;
		final int showGRAPH = 2;
		final int inqueryofBRIDGEWORDS = 3;
		final int produceNEWTEXT = 4;
		final int calSHORTPATH = 5;
		final int randomWALK = 6;
		final int qUIT = 0;
		int itemSelected;
		Scanner in = new Scanner(System.in);
		Graph gG = new Graph();
		String word1, word2;
		final int[][] pP = null;
		while (true) {
			itemSelected = menu();
			switch (itemSelected) {
			case createGRAPH:
				if (gG.getVertexNum() != 0) {
					System.out.println("ͼ�Ѿ�������");
					break;
				}
				System.out.println("�������ļ�����");
				String fileName;
				if (!in.hasNextLine()) {
					break;
				}
				while ("".equals(fileName = in.nextLine())) {
				}
				Scanner fileInput = null;
				try {
					fileInput = new Scanner(Paths.get(fileName));
				} catch (IOException e) {
					System.err.println("�ļ�" + fileName + "δ�ҵ���");
					break;
				}
				fileInput = eraseSpecialChar(fileInput);
				if (fileInput.hasNext()) {
					word1 = fileInput.next().toLowerCase();
					while (fileInput.hasNext()) {
						word2 = fileInput.next().toLowerCase();
						gG.addEdge(word1, word2);
						word1 = word2;
					}
				}
				fileInput.close();
				System.out.println("ͼ������ɡ�");
				break;
			case showGRAPH:
				showDirectedGraph(gG);
				generatePicture(gG.graphDespForGv(), "E:\\graph.png");
				break;
			case inqueryofBRIDGEWORDS:
				System.out.println("������Ҫ��ѯ���������ʣ�����#������");
				while (in.hasNext()) {
					word1 = in.next();
					if (word1.equals("#")) {
						break;
					}
					if (!in.hasNext()) {
						break;
					}
					word2 = in.next();
					printBridgeWords(gG, word1, word2);
				}
				break;
			case produceNEWTEXT:
				System.out.println("Input a new line:");
				if (in.hasNextLine()) {
					String newLine;
					while ("".equals(newLine = in.nextLine())) {
					}
					//Scanner newLineInput = new Scanner(newLine);
					Scanner newLineInput = eraseSpecialChar(new Scanner(newLine));
					System.out.println("���ɵ����ı���");
					System.out.println(gG.generateNewText(newLineInput.nextLine()));
					newLineInput.close();
				}
				break;
			case calSHORTPATH:
				boolean[][] outStand = new boolean[gG.getVertexNum()][gG.getVertexNum()];
				for (int i = 0; i < outStand.length; ++i) {
					Arrays.fill(outStand[i], false);
				}
				String desp;
				System.out.println("������Ҫ�������·�����������ʣ�����#������");
				System.out.println("��ֻ����һ������������#�����õ��ʵ��������е��ʵ����·����");
				while (in.hasNext()) {
					word1 = in.next();
					if (word1.equals("#")) {
						break;
					}
					word2 = in.next();
					if (word2.equals("#")) {
						//�����word1���������е��ʵ����·��
						for (int i = 0; i < gG.getVertexNum(); ++i) {
							word2 = gG.getString(i);
							if (!word2.equals(word1)) {
								System.out.println("��\"" + word1 + "\"��\"" + word2 + "\"�����·����");
								System.out.println(gG.calcShortestPath(word1, word2));
							}
						}
					} else {
						System.out.println("��\"" + word1 + "\"��\"" + word2 + "\"�����·����");
						System.out.println(gG.calcShortestPath(word1, word2));
						desp = despOfShortestPath(gG, pP, word1, word2, outStand);
						generatePicture(desp + gG.graphDespForGv(outStand), "E:\\shortest path.png");
					}
				}
				break;
			case randomWALK:
				String randomstring = gG.randomWalk();
				System.out.println("�������·����");
				System.out.println(randomstring);
				PrintWriter out = null;
				try {
					out = new PrintWriter("E:\\random walk.txt");
			        out.print(randomstring);
				} catch (IOException e) {
					System.err.println("�Ƿ��ļ�����");
					return;
				}
		        out.close();
				break;
			case qUIT:
				System.out.println("���������");
				in.close();
				System.exit(0);
				break;
			default:
				System.out.println("Input error!");
				break;
			}
		}
	}
	//��ʾ�˵�������û����������ѡ��
	/**
	 * @return a
	 */
	public static int menu() {
		Scanner in = new Scanner(System.in);
		System.out.println("��ѡ���ܣ�");
		System.out.println("1.�����ı�����������ͼ");
		System.out.println("2.չʾ����ͼ");
		System.out.println("3.��ѯ�ŽӴ�");
		System.out.println("4.�����ŽӴ��������ı�");
		System.out.println("5.������������֮������·��");
		System.out.println("6.�������");
		System.out.println("0.�˳�");
		if (in.hasNextInt()) {
			return in.nextInt();
		} else {
			return -1;
		}
	}
	//��in�а����ĳ���ĸ����ַ��滻Ϊ�ո񣬲�����
	/**
	 * @param in b
	 * @return a
	 */
	public static Scanner eraseSpecialChar(final Scanner in) {
		StringBuilder sBuilder = new StringBuilder();
		String s;
		while (in.hasNext()) {
			s = in.next();
			s = s.replaceAll("[^a-zA-Z]", " ");
			sBuilder.append(s + " ");
		}
		in.close();
		return new Scanner(sBuilder.toString());
	}
	//��ӡ����ͼ
	/**
	 * @param gG a
	 */
	public static void showDirectedGraph(final Graph gG) {
		gG.print();
	}
	//��ӡͼG��word1��word2���ŽӴʣ�
	//��ĳ���ʲ����������No "word1" in the graph! ��  No "word2" in the graph! ��  No "word1" and "word2" in the graph!
	//��������֮�䲻�����ŽӴ������No bridge from words "word1" to "word2"!
	//��������֮����1���ŽӴ������The bridge word from "word1" to "word2" is: xxx.
	//����������֮����2���������ŽӴ������The bridge words from "word1" to "word2" are: xxx, xxx, ... and xxx.
	/**
	 * @param gG h
	 * @param word1 j
	 * @param word2 w
	 */
	public static void printBridgeWords(final Graph gG, final String word1, final String word2) {
		Scanner rScanner = new Scanner(gG.queryBridgeWords(word1, word2));
		ArrayList<String> bridgeWords = new ArrayList<>();
		String res;
		int n = 0;
		if (rScanner.hasNextInt()) {
			n = rScanner.nextInt();
			if (n == 1) {
				res = "No \"" + word1;
				if (rScanner.hasNextInt()) {
					res += "\" and \"" + word2;
				}
				res += "\" in the graph!";
				System.out.println(res);
			} else {
				System.out.println("No \"" + word2 + "\" in the graph!");
			}
			rScanner.close();
			return;
		}
		while (rScanner.hasNext()) {
			bridgeWords.add(rScanner.next());
		}
		rScanner.close();
		if (bridgeWords.isEmpty()) {
			System.out.println("No bridge from words \"" + word1 + "\" to \"" + word2 + "\"!");
		} else if (bridgeWords.size() == 1) {
			System.out.println("The bridge word from \"" + word1 + "\" to \"" + word2 + "\" is: "
					+ bridgeWords.get(0) + ".");
		} else {
			res = "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: ";
			for (int i = 0; i < bridgeWords.size(); ++i) {
				res += bridgeWords.get(i);
				if (i < bridgeWords.size() - 2) {
					res += ", ";
				} else if (i == bridgeWords.size() - 2) {
					res += " and ";
				} else if (i == bridgeWords.size() - 1) {
					res += ".";
				}
			}
			System.out.println(res);
		}
	}
	//Ϊ��ͼƬ��ͻ����ʾ���·������outStand���󲢷�������GraphViz�������ߵ���ɫ���ַ���
	/**
	 * @param gG l
	 * @param pP o
	 * @param word1 v
	 * @param word2 k
	 * @param outStand u
	 * @return 1
	 */
	public static String despOfShortestPath(final Graph gG, final int[][] pP, final String word1, final String word2, final boolean[][] outStand) {
		ArrayList<Integer> path = new ArrayList<>();
		String desp = new String();
		int v1 = gG.findVertex(word1), v2 = gG.findVertex(word2);
		if (v1 < 0 || v2 < 0) {
			return "";
		}
		gG.floydCalcPath(v1, v2, path);
		for (int i = 0; i < path.size(); ++i) {
			desp += gG.getString(path.get(i)) + " [color=\"red\"];";
			if (i < path.size() - 1) {
				outStand[path.get(i)][path.get(i + 1)] = true;
			}
		}
		return desp;
	}
	//��GraphViz����ͼƬ�ļ���desp�������ߵ��ַ���
	/**
	 * @param desp aaa
	 * @param pictureFileName sss
	 */
	public static void generatePicture(final String desp, final String pictureFileName) {
		GraphViz gv = new GraphViz();
		gv.addln(gv.startgraph() + desp + gv.endgraph());
		gv.increaseDpi();
		File outfile = new File(pictureFileName);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), "png", "dot"), outfile);
		System.out.println("������ͼƬ" + pictureFileName);
	}

}

//�����Ȩͼ���ڽӱ��ʾ��
/**
 * @author asus aaa
 * sss.
 */
class Graph {
	//�߱���
	/**
	 * @author asus aaa
	 * sss.
	 */
	private class EdgeNode {
		/**
		 * aaa.
		 */
		private int adjvex, weight;
		/**
		 * aaa.
		 */
		private EdgeNode next;
		/**
		 * @param a aaa
		 * @param w sss
		 */
		EdgeNode(final int a, final int w) {
			adjvex = a; weight = w; }
	}

	//�������
	/**
	 * @author asus aaa
	 * sss.
	 */
	private class VertexNode {
		/**
		 * aaa.
		 */
		private String data;
		/**
		 * aaa.
		 */
		private int outdegree;
		/**
		 * aaa.
		 */
		private EdgeNode firstEdge;
		/**
		 * @param s aaa
		 */
		VertexNode(final String s) {
			data = s; }
	}
	/**
	 *  aaa.
	 */
	private ArrayList<VertexNode> vertexList = new ArrayList<>();
	/**
	 *  aaa.
	 */
	private int[][] pP = null;
	/**
	 * @return aaa
	 */
	public int getVertexNum() {
		return vertexList.size(); }
	/**
	 * @param v aaa
	 * @return aaa
	 */
	public String getString(final int v) {
		return vertexList.get(v).data; }
	/**
	 * @return aaa
	 */
	public int[][] getP() {
		return pP; }
	/**
	 * @param s aaa
	 * @return sss
	 */
	public int findVertex(final String s) {
		for (int i = 0; i < vertexList.size(); ++i) {
			if (vertexList.get(i).data.equals(s)) {
				return i;
			}
		}
		return -1;
	}
	//���ر�(v1->v2)��Ȩֵ��������Ƿ��򷵻�-1�����Ϸ��������ڴ˱��򷵻�Integer.MAX_VALUE����v1==v2�򷵻�0
	/**
	 * @param v1 a
	 * @param v2 s
	 * @return d
	 */
	public int weight(final int v1, final int v2) {
		if (v1 < 0 || v1 >= vertexList.size() || v2 < 0 || v2 >= vertexList.size()) {
			return -1;
		} else if (v1 == v2) {
			return 0;
		}
		for (EdgeNode edge = vertexList.get(v1).firstEdge; edge != null; edge = edge.next) {
			if (edge.adjvex == v2) {
				return edge.weight;
			}
		}
		return Integer.MAX_VALUE;
	}
	/**
	 * @param s1 a
	 * @param s2 s
	 * @return a
	 */
	public int weight(final String s1, final String s2) {
		return weight(findVertex(s1), findVertex(s2));
	}

	//�����Ƿ���ڱ�(v1->v2)
	/**
	 * @param v1 aaa
	 * @param v2 ss
	 * @return dd
	 */
	private EdgeNode findEdge(final int v1, final int v2) {
		if (v1 < 0 || v1 >= vertexList.size() || v2 < 0 || v2 >= vertexList.size() || v1 == v2) {
			return null;
		}
		EdgeNode edge = null;
		for (edge = vertexList.get(v1).firstEdge; edge != null && edge.adjvex != v2; edge = edge.next) {
		}
		return edge;
	}
	//�����Ƿ���ڱ�(s1->s2)
	/**
	 * @param s1 aaa
	 * @param s2 sss
	 * @return c
	 */
	public boolean findEdge(final String s1, final String s2) {
		int v1 = findVertex(s1);
		int v2 = findVertex(s2);
		return findEdge(v1, v2) != null;
	}
	//��ӱ�(v1->v2)��v1, v2����Ϊ�Ѵ����±꣬�����Ѵ�����Ȩֵ��1
	/**
	 * @param v1  sss
	 * @param v2 aaa
	 */
	private void addEdge(final int v1, final int v2) {
		if (v1 < 0 || v1 >= vertexList.size() || v2 < 0 || v2 >= vertexList.size() || v1 == v2) {
			return;
		}
		EdgeNode edge = findEdge(v1, v2);
		if (edge != null) {
			edge.weight++;
		} else {
			edge = new EdgeNode(v2, 1);
			edge.next = vertexList.get(v1).firstEdge;
			vertexList.get(v1).firstEdge = edge;
			vertexList.get(v1).outdegree++;
		}
	}
	//��ӱ�(s1->s2)���������ڶ�������ӵ�vertexList
	/**
	 * @param s1 aaa
	 * @param s2 sss
	 */
	public void addEdge(final String s1, final String s2) {
		int n1 = findVertex(s1), n2 = findVertex(s2);
		if (n1 < 0) {
			vertexList.add(new VertexNode(s1));
			n1 = vertexList.size() - 1;
		}
		if (n2 < 0) {
			vertexList.add(new VertexNode(s2));
			n2 = vertexList.size() - 1;
		}
		addEdge(n1, n2);
	}
	//��ӡ����ͼ
	/**
	 * aaa.
	 */
	public void print() {
		for (VertexNode v : vertexList) {
			System.out.print(v.data + "->[");
			for (EdgeNode e = v.firstEdge; e != null; e = e.next) {
				System.out.print(vertexList.get(e.adjvex).data + "(" + e.weight + ") ");
			}
			System.out.println("]");
		}
	}
	//��Floyd�㷨��ͼG�����������������·������
	//A(k)[i][j]Ϊ�Ӷ���i������ֻ������Ų�����k���м䶥�㵽�ﶥ��j�����·�����ȣ�A(-1)[i][j]=weight(i,j)
	//��Ӷ���i��j�����·������ΪA(n-1)[i][j]
	//A(k)[i][j]=min{A(k-1)[i][j],A(k-1)[i][k]+A(k-1)[k][j]}
	//��A(k)[i][k]=A(k-1)[i][k],A(k)[k][j]=A(k-1)[k][j]
	//P[i][j]==k��ʾ��i��j�����·���辭��k������˵����ǰ�����㣩
	//��Ϊ-1���ʾû��·������Ϊj���ʾ��ֱ�������ı߶���������������
	/**
	 * @return aaa
	 */
	int[][] floyd() {
		int n = getVertexNum();
		int[][] aA = new int[n][n];
		pP = new int[n][n];
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				aA[i][j] = weight(i, j);
				if (aA[i][j] < Integer.MAX_VALUE) {
					pP[i][j] = j;
				} else {
					pP[i][j] = -1;
				}
			}
		}
		for (int k = 0; k < n; ++k) {
			for (int i = 0; i < n; ++i) {
				for (int j = 0; j < n; ++j) {
					if ((long) aA[i][k] + (long) aA[k][j] < (long) aA[i][j]) {
						aA[i][j] = aA[i][k] + aA[k][j];
						pP[i][j] = k;
				    }
				}
			}
		}
		return pP;
	}
	//����Floyd�㷨������ľ���P�󶥵�src��dst�����·����������path��
	//��P[src][dst]==-1���ʾ�б�(src->dst)��ֻ��path�м���dst��
	//����ݹ����floyd_calc_path(src,P[src][dst])��floyd_calc_path(P[src][dst],dst)
	/**
	 * @param src  aaa
	 * @param dst  sss
	 * @param path ddd
	 */
	public void floydCalcPath(final int src, final int dst, final ArrayList<Integer> path) {
		if (pP == null || pP.length != vertexList.size()) {
			pP = floyd();
		}
		if (pP[src][dst] == -1) {
			return;
		}
		if (path.isEmpty()) {
			path.add(src);
		}
		if (pP[src][dst] == dst) {
			path.add(dst);
		} else {
			floydCalcPath(src, pP[src][dst], path);
			floydCalcPath(pP[src][dst], dst, path);
		}
	}
	//����floydCalcPath()���㵥��word1��word2�����·�������ַ�����ʽ����
	/**
	 * @param word1  sss
	 * @param word2  aaa
	 * @return    ddd
	 */
	public String calcShortestPath(final String word1, final String word2) {
		ArrayList<Integer> path = new ArrayList<>();
		String res = null;
		int v1 = findVertex(word1), v2 = findVertex(word2);
		if (v1 < 0 || v2 < 0) {
			return "No path.";
		}
		floydCalcPath(v1, v2, path);
		for (int i = 0; i < path.size(); ++i) {
			if (i > 0) {
				res += "->";
			}
			res += vertexList.get(path.get(i)).data;
		}
		return res;
	}
	//��������ͼƬ�ļ�����ıߵ���Ϣ
	/**
	 * @return aaa
	 */
	public String graphDespForGv() {
		StringBuilder s = new StringBuilder();
		for (VertexNode v : vertexList) {
			String one = v.data;
			for (EdgeNode e = v.firstEdge; e != null; e = e.next) {
				s.append(one + "->" + vertexList.get(e.adjvex).data + "[label = " + e.weight + "];");
			}
		}
		return s.toString();
	}

	//��������ͼƬ�ļ�����ıߵ���Ϣ��outStand��ǵ����·���ú�ɫ��ʾ��
	/**
	 * @param outStand aaa
	 * @return sss
	 */
	public String graphDespForGv(final boolean[][] outStand) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < getVertexNum(); i++) {
			VertexNode v = vertexList.get(i);
			String one = v.data;
			for (EdgeNode e = v.firstEdge; e != null; e = e.next) {
				s.append(one + "->" + vertexList.get(e.adjvex).data + "[");
				if (outStand[i][e.adjvex]) {
					s.append("color=\"red\",");
				}
				s.append("label = " + e.weight + "];");
			}
		}
		return s.toString();
	}
	//��ѯ����word1��word2���ŽӴʲ����أ����ж�����ÿո����
	/**
	 * @param word1 ab
	 * @param word2 aaa
	 * @return dd
	 */
	public String queryBridgeWords(final String word1, final String word2) {
		int v1 = findVertex(word1), v2 = findVertex(word2);
		String res = new String();
		if (v1 < 0) {
			res = "1";
			if (v2 < 0) {
				res += " 2";
			}
			return res;
		}
		for (EdgeNode e = vertexList.get(v1).firstEdge; e != null; e = e.next) {
			if (findEdge(v1, e.adjvex) != null && findEdge(e.adjvex, v2) != null) {
				res += vertexList.get(e.adjvex).data + " ";
			}
		}
		return res;
	}
	//�����ŽӴ��������ı�����ÿ�������ڵĵ���֮ǰ�������ŽӴ�
	/**
	 * @param inputText aaa
	 * @return dd
	 */
	public String generateNewText(final String inputText) {
		StringBuilder inBuilder = new StringBuilder();
		Scanner in = new Scanner(inputText);
		String word1 = null, word2 = null, result = null;
		if (!in.hasNext()) {
			in.close();
			return "";
		}
		word1 = in.next().toLowerCase();
		inBuilder.append(word1 + " ");
		while (in.hasNext()) {
			word2 = in.next().toLowerCase();
			result = queryBridgeWords(word1, word2);
			Scanner newWord = new Scanner(result);
			if (!result.equals("") && !newWord.hasNextInt()) {
				inBuilder.append(newWord.next() + " ");
				newWord.close();
			}
			inBuilder.append(word2 + " ");
			word1 = word2;
		}
		in.close();
		return inBuilder.toString();
	}
	//������ߣ�������û���ڽӵ�Ķ�����Ѿ��߹��ı���ֹͣ�����ַ�����ʽ�����߹���·��
	/**
	 * @return aaa
	 */
	public String randomWalk() {
		boolean[][] visited = new boolean[vertexList.size()][vertexList.size()];
		StringBuilder s = new StringBuilder();
		Random r = new java.util.Random();
		int num = r.nextInt(vertexList.size());
		while (true) {
			int prenum = num;
			EdgeNode preNode = vertexList.get(num).firstEdge;
		    s.append(vertexList.get(num).data);
            if (preNode == null) {
            	break;
            }
			num = r.nextInt(vertexList.get(num).outdegree);
			while (num > 0) {
				preNode = preNode.next;
			    num--;
			}
			num = preNode.adjvex;
			if (visited[prenum][num]) {
				s.append("->" + vertexList.get(num).data);
				break;
			}
			visited[prenum][num] = true;
			s.append("->");
		}
		return s.toString();
	}
}
