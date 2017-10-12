import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;//import
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;//import

public class PairProgramming
{
	
	public static void main(String[] args)//main
	{
		int itemSelected;
		Scanner in = new Scanner(System.in);
		Graph G = new Graph();
		String word1, word2;
		int[][] P = null;
		while (true) {
			itemSelected = menu();
			switch (itemSelected) {
			case 1://����1
				if (G.getVertexNum() != 0) {
					System.out.println("ͼ�Ѿ�������");
					break;
				}
				System.out.println("�������ļ�����");
				String fileName;
				if (!in.hasNextLine()) break;
				while ("".equals(fileName = in.nextLine())) ;
				Scanner fileInput = null;
				try {
					fileInput = new Scanner(Paths.get(fileName));
				}
				catch (IOException e) {
					System.err.println("�ļ�" + fileName + "δ�ҵ���");
					break;
				}
				
				fileInput = eraseSpecialChar(fileInput);
				if (fileInput.hasNext()) {
					word1 = fileInput.next().toLowerCase();
					while (fileInput.hasNext()) {
						word2 = fileInput.next().toLowerCase();
						G.addEdge(word1, word2);
						word1 = word2;
					}
				}
				fileInput.close();
				System.out.println("ͼ������ɡ�");
				break;
			case 2://����2
				showDirectedGraph(G);
				generatePicture(G.graphDespForGv());
				break;
			case 3://����3
				System.out.println("������Ҫ��ѯ���������ʣ�����#������");
				while (in.hasNext()) {
					word1 = in.next();
					if (word1.equals("#")) break;
					if (!in.hasNext()) break;
					word2 = in.next();
					printBridgeWords(G, word1, word2);
				}
				break;
			case 4://����4
				System.out.println("Input a new line:");
				if (in.hasNextLine()) {
					String newLine;
					while ("".equals(newLine = in.nextLine())) ;	//��ȡ����
					//Scanner newLineInput = new Scanner(newLine);
					Scanner newLineInput = eraseSpecialChar(new Scanner(newLine));
					System.out.println("���ɵ����ı���");
					System.out.println(G.generateNewText(newLineInput.nextLine()));
					newLineInput.close();
				}
				break;
			case 5://����5
				boolean[][] outStand = new boolean[G.getVertexNum()][G.getVertexNum()];
				for (int i = 0; i < outStand.length; ++i) Arrays.fill(outStand[i], false);
				String desp;
				System.out.println("������Ҫ�������·�����������ʣ�����#������");
				System.out.println("��ֻ����һ������������#�����õ��ʵ��������е��ʵ����·����");
				while (in.hasNext()) {
					word1 = in.next();
					if (word1.equals("#")) break;
					word2 = in.next();
					if (word2.equals("#")) {
						//�����word1���������е��ʵ����·��
						for (int i = 0; i < G.getVertexNum(); ++i) {
							word2 = G.getString(i);
							if (!word2.equals(word1)) {
								System.out.println("��\"" + word1 + "\"��\"" + word2 + "\"�����·����");
								System.out.println(G.calcShortestPath(word1, word2));
							}
						}
					}
					else {
						System.out.println("��\"" + word1 + "\"��\"" + word2 + "\"�����·����");
						System.out.println(G.calcShortestPath(word1, word2));
						desp = despOfShortestPath(G, P, word1, word2, outStand);
						generatePicture(desp + G.graphDespForGv(outStand));
					}
				}
				break;
			case 6://����6
				String randomstring = G.randomWalk();
				System.out.println("�������·����");
				System.out.println(randomstring);
				PrintWriter out = null;
				try {
					out = new PrintWriter("E:\\2.txt");
			        out.print(randomstring);
				}
				catch(IOException e) {
					System.err.println("�Ƿ��ļ�����");
				}
		        out.close();
				break;
			case 0:
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
	public static int menu()
	{
		Scanner in = new Scanner(System.in);
		System.out.println("��ѡ���ܣ�");
		System.out.println("1.�����ı�����������ͼ");
		System.out.println("2.չʾ����ͼ");
		System.out.println("3.��ѯ�ŽӴ�");
		System.out.println("4.�����ŽӴ��������ı�");
		System.out.println("5.������������֮������·��");
		System.out.println("6.�������");
		System.out.println("0.�˳�");
		if (in.hasNextInt()) return in.nextInt();
		else return -1;
	}
	
	//��in�а����ĳ���ĸ����ַ��滻Ϊ�ո񣬲�����
	public static Scanner eraseSpecialChar(Scanner in)
	{
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
	public static void showDirectedGraph(Graph G)
	{
		G.print();
	}
	
	//��ӡͼG��word1��word2���ŽӴʣ�
	//��ĳ���ʲ����������No "word1" in the graph! ��  No "word2" in the graph! ��  No "word1" and "word2" in the graph!
	//��������֮�䲻�����ŽӴ������No bridge from words "word1" to "word2"!
	//��������֮����1���ŽӴ������The bridge word from "word1" to "word2" is: xxx.
	//����������֮����2���������ŽӴ������The bridge words from "word1" to "word2" are: xxx, xxx, ... and xxx.
	public static void printBridgeWords(Graph G, String word1, String word2)
	{
		Scanner rScanner = new Scanner(G.queryBridgeWords(word1, word2));
		ArrayList<String> bridgeWords = new ArrayList<>();
		String res = new String();
		int n = 0;
		if (rScanner.hasNextInt()) {
			n = rScanner.nextInt();
			if (n == 1) {
				res = "No \"" + word1;
				if (rScanner.hasNextInt()) res += "\" and \"" + word2;
				res += "\" in the graph!";
				System.out.println(res);
			}
			else System.out.println("No \"" + word2 + "\" in the graph!");
			rScanner.close();
			return;
		}
		
		while (rScanner.hasNext()) bridgeWords.add(rScanner.next());
		rScanner.close();
		if (bridgeWords.isEmpty())
			System.out.println("No bridge from words \"" + word1 + "\" to \"" + word2 + "\"!");
		else if (bridgeWords.size() == 1)
			System.out.println("The bridge word from \"" + word1 + "\" to \"" + word2 + "\" is: " +
				bridgeWords.get(0) + ".");
		else {
			res = "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: ";
			for (int i = 0; i < bridgeWords.size(); ++i) {
				res += bridgeWords.get(i);
				if (i < bridgeWords.size() - 2) res += ", ";
				else if (i == bridgeWords.size() - 2) res += " and ";
				else if (i == bridgeWords.size() - 1) res += ".";
			}
			System.out.println(res);
		}
	}
	
	//Ϊ��ͼƬ��ͻ����ʾ���·������outStand���󲢷�������GraphViz�������ߵ���ɫ���ַ���
	public static String despOfShortestPath(Graph G, int[][] P, String word1, String word2, boolean[][] outStand)
	{
		ArrayList<Integer> path = new ArrayList<>();
		String desp = new String();
		int v1 = G.findVertex(word1), v2 = G.findVertex(word2);
		if (v1 < 0 || v2 < 0) return "";
		G.floydCalcPath(v1, v2, path);
		
		for (int i = 0; i < path.size(); ++i) {
			desp += G.getString(path.get(i)) + " [color=\"red\"];";
			if (i < path.size() - 1)
				outStand[path.get(i)][path.get(i + 1)] = true;
		}
		return desp;
	}
	
	//��GraphViz����ͼƬ�ļ���desp�������ߵ��ַ���
	public static void generatePicture(String desp)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph() + desp + gv.end_graph());
		gv.increaseDpi();
		File outfile = new File("E:\\out.png");
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), "png", "dot"), outfile);
		System.out.println("������ͼƬE:\\out.png");
	}

}

//�����Ȩͼ���ڽӱ��ʾ��
class Graph//ͼ��
{
	//�߱���
	private class EdgeNode
	{
		int adjvex, weight;
		EdgeNode next;
		
		public EdgeNode(int a, int w) { adjvex = a; weight = w; }
	}

	//�������
	private class VertexNode
	{
		String data;
		int outdegree;
		EdgeNode firstEdge;
		
		public VertexNode(String s) { data = s; }
	}
	
	private ArrayList<VertexNode> vertexList = new ArrayList<>();
	private int[][] P = null;
	
	public int getVertexNum() { return vertexList.size(); }
	public String getString(int v) { return vertexList.get(v).data; }
	public int[][] getP() { return P; }
	public int findVertex(String s)
	{
		for (int i = 0; i < vertexList.size(); ++i)
			if (vertexList.get(i).data.equals(s)) return i;
		return -1;
	}
	
	//���ر�(v1->v2)��Ȩֵ��������Ƿ��򷵻�-1�����Ϸ��������ڴ˱��򷵻�Integer.MAX_VALUE����v1==v2�򷵻�0
	public int weight(int v1, int v2)
	{
		if (v1 < 0 || v1 >= vertexList.size() || v2 < 0 || v2 >= vertexList.size()) return -1;
		else if (v1 == v2) return 0;
		for (EdgeNode edge = vertexList.get(v1).firstEdge; edge != null; edge = edge.next)
			if (edge.adjvex == v2) return edge.weight;
		return Integer.MAX_VALUE;
	}
	public int weight(String s1, String s2)
	{
		return weight(findVertex(s1), findVertex(s2));
	}

	//�����Ƿ���ڱ�(v1->v2)
	private EdgeNode findEdge(int v1, int v2)
	{
		if (v1 < 0 || v1 >= vertexList.size() || v2 < 0 || v2 >= vertexList.size() || v1 == v2) return null;
		EdgeNode edge = null;
		for (edge = vertexList.get(v1).firstEdge; edge != null && edge.adjvex != v2; edge = edge.next) ;
		return edge;
	}
	//�����Ƿ���ڱ�(s1->s2)
	public boolean findEdge(String s1, String s2)
	{
		int v1 = findVertex(s1), v2 = findVertex(s2);
		return findEdge(v1, v2) != null;
	}
	
	//��ӱ�(v1->v2)��v1, v2����Ϊ�Ѵ����±꣬�����Ѵ�����Ȩֵ��1
	private void addEdge(int v1, int v2)
	{
		if (v1 < 0 || v1 >= vertexList.size() || v2 < 0 || v2 >= vertexList.size() || v1 == v2) return;
		EdgeNode edge = findEdge(v1, v2);
		if (edge != null) edge.weight++;
		else {
			edge = new EdgeNode(v2, 1);
			edge.next = vertexList.get(v1).firstEdge;
			vertexList.get(v1).firstEdge = edge;
			vertexList.get(v1).outdegree++;
		}
	}
	//��ӱ�(s1->s2)���������ڶ�������ӵ�vertexList
	public void addEdge(String s1, String s2)
	{
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
	
	public void print()
	{
		for (VertexNode v : vertexList) {
			System.out.print(v.data + "->[");
			for (EdgeNode e = v.firstEdge; e != null; e = e.next)
				System.out.print(vertexList.get(e.adjvex).data + "(" + e.weight + ") ");
			System.out.println("]");
		}
	}
	
	//��Floyd�㷨��ͼG�����������������·������
	//A(k)[i][j]Ϊ�Ӷ���i������ֻ������Ų�����k���м䶥�㵽�ﶥ��j�����·�����ȣ�A(-1)[i][j]=weight(i,j)
	//��Ӷ���i��j�����·������ΪA(n-1)[i][j]
	//A(k)[i][j]=min{A(k-1)[i][j],A(k-1)[i][k]+A(k-1)[k][j]}
	//��A(k)[i][k]=A(k-1)[i][k],A(k)[k][j]=A(k-1)[k][j]
	//P[i][j]==k��ʾ��i��j�����·���辭��k������˵����ǰ�����㣩
	//��Ϊ-2���ʾû��·������Ϊ-1���ʾ��ֱ�������ı߶���������������
	int[][] floyd()
	{
		int n = getVertexNum();
		int[][] A = new int[n][n];
		P = new int[n][n];
		for (int i = 0; i < n; ++i)
			for (int j = 0; j < n; ++j) {
				A[i][j] = weight(i, j);
				P[i][j] = A[i][j] < Integer.MAX_VALUE ? -1 : -2;
			}
		
		for (int k = 0; k < n; ++k)
			for (int i = 0; i < n; ++i)
				for (int j = 0; j < n; ++j)
					if ((long) A[i][k] + (long) A[k][j] < (long) A[i][j]) {
						A[i][j] = A[i][k] + A[k][j];
						P[i][j] = k;
					}
		return P;
	}
	
	//����Floyd�㷨������ľ���P�󶥵�src��dst�����·����������path��
	//��P[src][dst]==-1���ʾ�б�(src->dst)��ֻ��path�м���dst��
	//����ݹ����floyd_calc_path(src,P[src][dst])��floyd_calc_path(P[src][dst],dst)
	public void floydCalcPath(int src, int dst, ArrayList<Integer> path)
	{
		if (P[src][dst] == -2) return;
		if (path.isEmpty()) path.add(src);
		if (P[src][dst] == -1) path.add(dst);
		else {
			floydCalcPath(src, P[src][dst], path);
			floydCalcPath(P[src][dst], dst, path);
		}
	}
	
	public String calcShortestPath(String word1, String word2)
	{
		ArrayList<Integer> path = new ArrayList<>();
		String res = new String();
		int v1 = findVertex(word1), v2 = findVertex(word2);
		if (v1 < 0 || v2 < 0) return "No path.";
		if (P == null || P.length != vertexList.size()) P = floyd();
		floydCalcPath(v1, v2, path);
		
		for (int i = 0; i < path.size(); ++i) {
			if (i > 0) res += "->";
			res += vertexList.get(path.get(i)).data;
		}
		return res;
	}
	
	public String graphDespForGv()
	{
		StringBuilder s = new StringBuilder();
		for (VertexNode v : vertexList) {
			String one = v.data;
			for (EdgeNode e = v.firstEdge; e != null; e = e.next)
				s.append(one + "->" + vertexList.get(e.adjvex).data + "[label = " + e.weight + "];");
		}
		return s.toString();
	}
	
	public String graphDespForGv(boolean[][] outStand)
	{
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < getVertexNum(); i++) {
			VertexNode v = vertexList.get(i);
			String one = v.data;
			for (EdgeNode e = v.firstEdge; e != null; e = e.next) {
				s.append(one + "->" + vertexList.get(e.adjvex).data + "[");
				if (outStand[i][e.adjvex]) s.append("color=\"red\",");
				s.append("label = " + e.weight + "];");
			}
		}
		return s.toString();
	}
	
	public String queryBridgeWords(String word1, String word2)
	{
		int v1 = findVertex(word1), v2 = findVertex(word2);
		String res = new String();
		if (v1 < 0) {
			res = "1";
			if (v2 < 0) res += " 2";
			return res;
		}
		else if (v2 < 0) return "2";
		
		for (EdgeNode e = vertexList.get(v1).firstEdge; e != null; e = e.next)
			if (findEdge(v1, e.adjvex) != null && findEdge(e.adjvex, v2) != null)
				res += vertexList.get(e.adjvex).data + " ";
		return res;
	}
	
	public String generateNewText(String inputText)
	{
		StringBuilder inBuilder = new StringBuilder();
		Scanner in = new Scanner(inputText);
		String word1 = null, word2 = null, result = null;
		if (!in.hasNext()) {
			in.close();
			return "";
		}
		word1 = in.next();
		inBuilder.append(word1 + " ");
		
		while(in.hasNext()) {
			word2 = in.next();
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
	
	public String randomWalk()
	{
		boolean[][] visited = new boolean[vertexList.size()][vertexList.size()];
		StringBuilder s = new StringBuilder();
		Random r = new java.util.Random();
		int num = r.nextInt(vertexList.size());
		while(true) {
			int prenum = num;
			EdgeNode preNode = vertexList.get(num).firstEdge;
		    s.append(vertexList.get(num).data);
            if(preNode == null) break;
            
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
