import java.util.Scanner;

public class RunThis {
	public static void main(String[] args) {
		System.out.println("Enter all values: ");
		Scanner in = new Scanner(System.in);
		int[][] values = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				values[i][j] = in.nextInt();				
			}
		}
		
	}
}
