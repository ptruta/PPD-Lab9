public class Product extends Thread {
    int[][] matrix1;
    int[][] matrix2;
    int[][] result;
    int startN;
    int endN;
    int startM;
    int endM;
    int m;
    int n;

    public Product(int[][] matrix1, int[][] matrix2, int[][] result,int n, int m, int start, int end, int startM, int endM) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
        this.startN = start;
        this.endN = end;
        this.startM = startM;
        this.endM = endM;
        this.n = n;
        this.m = m;
    }
    public void run(){
        for (int i=startN;i<endN; i++){
            for (int j=startM; j< endM;j++){
                int computed=0;
                for (int k = 0; k<m;k++){
                    for (int l=0;l<n;l++){
                        computed+=matrix1[i][k]*matrix2[l][j];
                    }
                }

                this.result[i][j] = computed;
            }
        }

    }
}
