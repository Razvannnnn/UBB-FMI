package ppd;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static class CyclicWorker extends Thread{
        private int id,n,p;
        private int[] A,B,C;

        public CyclicWorker(int id, int n, int p, int[] a, int[] b, int[] c) {
            this.id = id;
            this.n = n;
            this.p = p;
            A = a;
            B = b;
            C = c;
        }

        public void run(){
            for(int i=id;i<n;i+=p){
                C[i] = A[i] + B[i];
            }
        }

    }
    public static class BlockWorker extends Thread{
        private int start,end;
        private int[] A,B,C;

        public BlockWorker(int start, int end, int[] a, int[] b, int[] c) {
            this.start = start;
            this.end = end;
            A = a;
            B = b;
            C = c;
        }

        public void run(){
            for(int i=start;i<end;i++){
                C[i] = A[i] + B[i];
            }
        }
    }
    public static int[] generator(int n, int max){
        int [] v=new int[n];
        Random rand = new Random();
        for(int i=0;i<n;i++){
            v[i]=rand.nextInt(max);
        }
        return v;
    }
    //secvential
    static long runSequential(int[] A, int[] B, int[] C){
        long t0=System.nanoTime();
        for(int i=0;i<A.length;i++){
            C[i]=A[i]+B[i];
        }
        long t1=System.nanoTime();
        return t1-t0;
    }

    static long runCyclic(int[] A, int[] B, int[] C, int p) throws InterruptedException{
        CyclicWorker[] thread = new CyclicWorker[p];
        long t0 = System.nanoTime();
        for (int id = 0; id < p; id++) {
            thread[id] = new CyclicWorker(id, A.length, p, A, B, C);
            thread[id].start();
        }
        for (int id = 0; id < p; id++) {
            thread[id].join();
        }
        long t1 = System.nanoTime();
        return t1 - t0;
    }

    static long runBlock(int []A, int[]B, int[]C, int p) throws InterruptedException{
        BlockWorker[] thread = new BlockWorker[p];
        long t0 = System.nanoTime();
        // n=10 p=3
        // id =0 -> 0,1,2
        // id =1 -> 3,4,5
        // id =2 -> 7,8,9
        for(int i=0;i<p;i++){
            int start= i*p;
            int end= start+p;
            if(i==p-1){
                end=A.length;
            }
            thread[i] = new BlockWorker(start,end,A,B,C);
            thread[i].start();
        }
        for(int i=0;i<p;i++){
            thread[i].join();
        }
        long t1 = System.nanoTime();
        return t1 - t0;
    }
    public static void main(String[] args) throws InterruptedException {
        int n=1_000_000;
        int p=3;
        int max=50_000;

        int[] A=generator(n,max);
        int[] B=generator(n,max);

        //secvential
        int[] C1=new int[n];
        long tSeq=runSequential(A,B,C1);
        System.out.println("Sequential: "+tSeq);
        //cyclic
        int[] C2=new int[n];
        long tCyclic=runCyclic(A,B,C2,p);
        System.out.println("Cyclic: "+tCyclic);
        //block
        int[] C3=new int[n];
        long tBlock=runBlock(A,B,C3,p);
        System.out.println("Block: "+tBlock);

        System.out.println("C1==C2 ? "+ Arrays.equals(C1,C2));
        System.out.println("C1==C3 ? "+ Arrays.equals(C1,C3));
    }
}