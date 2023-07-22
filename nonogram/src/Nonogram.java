import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Nonogram {

    static Scanner sc;

    static final char x = 'x';
    static final char o = 'o';
    static final char n = ' ';

    static int numOfRows;
    static int numOfCols;
    static char[][] arr;
    static int remainOdds = 0;

    static int[][] rowsQueries;
    static int[][] colsQueries;

    static LinkedList<boolean[]>[] rowsOdds;
    static LinkedList<boolean[]>[] colsOdds;

    static final boolean row = true, col = false;

    public static void main(String[] args) {

        initiate();

        getQueries();

        getAllOdds();

        sureAll();

        int numOfFinalOdds = numOfCols + numOfRows;

        while (remainOdds > numOfFinalOdds) {
            filterAll();
            sureAll();
        }

        print();
    }

    static void initiate() {
        sc = new Scanner(System.in);
        System.out.print("enter num of rows : ");
        numOfRows = sc.nextInt();
        System.out.print("enter num of cols : ");
        numOfCols = sc.nextInt();
        arr = new char[numOfRows][numOfCols];
        rowOdd = new boolean[numOfCols];
        colOdd = new boolean[numOfRows];
        sc.nextLine();
        System.out.println();

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                arr[i][j] = n;
            }
        }
    }

    static void getQueries() {
        rowsQueries = new int[numOfRows][];
        colsQueries = new int[numOfCols][];

        // for rows
        for (int i = 0; i < numOfRows; i++) {
            System.out.printf("enter query for row #%3d : ", i + 1);
            String line = sc.nextLine();

            StringTokenizer st = new StringTokenizer(line);
            rowsQueries[i] = new int[st.countTokens()];
            for (int j = 0; st.hasMoreTokens(); j++) {
                rowsQueries[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        System.out.println();

        // for cols
        for (int i = 0; i < numOfCols; i++) {
            System.out.printf("enter query for col #%3d : ", i + 1);
            String line = sc.nextLine();

            StringTokenizer st = new StringTokenizer(line);
            colsQueries[i] = new int[st.countTokens()];
            for (int j = 0; st.hasMoreTokens(); j++) {
                colsQueries[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        System.out.println();
        System.out.println();
    }

    static void getAllOdds() {
        rowsOdds = new LinkedList[numOfRows];
        colsOdds = new LinkedList[numOfCols];

        // for rows
        for (int i = 0; i < numOfRows; i++)
            rowsOdds[i] = new LinkedList<>();

        // for cols
        for (int i = 0; i < numOfCols; i++)
            colsOdds[i] = new LinkedList<>();


        // for rows
        for (int i = 0; i < numOfRows; i++)
            getOdds(row, rowsQueries[i], 0, 0, rowsOdds[i], getLength(rowsQueries[i]));

        // for cols
        for (int i = 0; i < numOfCols; i++)
            getOdds(col, colsQueries[i], 0, 0, colsOdds[i], getLength(colsQueries[i]));
    }

    static boolean[] rowOdd;

    static boolean[] colOdd;

    static void getOdds(boolean row, int[] query, int group, int start, LinkedList<boolean[]> ll, int restLength) {
        int end;
        if (row) {
            end = numOfCols - query[group] - restLength;

            for (int i = start; i < query[group] + start; i++)
                rowOdd[i] = true;

            for (int i = start; i <= end; i++) {

                if (group == query.length - 1) {
                    for (int j = query[group] + i; j < numOfCols; j++)
                        rowOdd[j] = false;

                    remainOdds++;
                    ll.add(rowOdd.clone());
                } else {
                    rowOdd[query[group] + i] = false;
                    getOdds(row, query, group + 1, i + query[group] + 1, ll, restLength - (query[group + 1] + 1));
                }

                if (i != end) {
                    rowOdd[query[group] + i] = true;
                    rowOdd[i] = false;
                }
            }
        } else {
            end = numOfRows - query[group] - restLength;

            for (int i = start; i < query[group] + start; i++)
                colOdd[i] = true;

            for (int i = start; i <= end; i++) {

                if (group == query.length - 1) {
                    for (int j = query[group] + i; j < numOfRows; j++)
                        colOdd[j] = false;

                    remainOdds++;
                    ll.add(colOdd.clone());
                } else {
                    colOdd[query[group] + i] = false;
                    getOdds(col, query, group + 1, i + query[group] + 1, ll, restLength - (query[group + 1] + 1));
                }

                if (i != end) {
                    colOdd[query[group] + i] = true;
                    colOdd[i] = false;
                }
            }
        }


    }

    static int getLength(int[] query) {
        int sum = 0;
        for (int i = 1; i < query.length; i++) {
            sum += query[i] + 1;
        }

        return sum;
    }

    static void sureAll() {
        for (int i = 0; i < numOfRows; i++)
            sure(row, i, rowsOdds[i]);

        for (int i = 0; i < numOfCols; i++)
            sure(col, i, colsOdds[i]);
    }

    static boolean choice;

    static void sure(boolean row, int location, LinkedList<boolean[]> odds) {
        if (row) {
            outer:
            for (int i = 0; i < numOfCols; i++) {
                choice = odds.get(0)[i];
                for (int j = 1; j < odds.size(); j++) {
                    if (choice != odds.get(j)[i]) continue outer;
                }

                if (choice) arr[location][i] = o;
                else arr[location][i] = x;
            }
        } else {
            outer:
            for (int i = 0; i < numOfRows; i++) {
                choice = odds.get(0)[i];
                for (int j = 1; j < odds.size(); j++) {
                    if (choice != odds.get(j)[i]) continue outer;
                }

                if (choice) arr[i][location] = o;
                else arr[i][location] = x;
            }
        }
    }

    static void filterAll() {
        for (int i = 0; i < numOfRows; i++)
            filter(row, i, rowsOdds[i]);


        for (int i = 0; i < numOfCols; i++)
            filter(col, i, colsOdds[i]);
    }

    static void filter(boolean row, int location, LinkedList<boolean[]> odds) {
        boolean tmp;

        if (row) {
            for (int i = 0; i < numOfCols; i++) {
                if (arr[location][i] == n) continue;

                tmp = arr[location][i] == o;

                for (int j = 0; j < odds.size(); j++) {
                    if (tmp != odds.get(j)[i]) {
                        odds.remove(j);
                        remainOdds--;
                        j--;
                    }
                }
            }
        } else {
            for (int i = 0; i < numOfRows; i++) {
                if (arr[i][location] == n) continue;

                tmp = arr[i][location] == o;

                for (int j = 0; j < odds.size(); j++) {
                    if (tmp != odds.get(j)[i]) {
                        odds.remove(j);
                        remainOdds--;
                        j--;
                    }
                }
            }
        }
    }

    static void print() {
        System.out.print("|  |");
        for (int i = 0; i < numOfCols; i++) {
            System.out.printf("%3d|", i + 1);
        }

        System.out.println();

        for (int i = 0; i < numOfRows; i++) {
            System.out.printf("|%2d|", i + 1);
            for (int j = 0; j < numOfCols; j++) {
                System.out.print(" " + arr[i][j] + " ");
                System.out.print('|');
            }
            System.out.println();
        }
    }
}