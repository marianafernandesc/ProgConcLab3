/* Disciplina: Computacao Concorrente */
/* Prof.: Silvana Rossetto */
/* Laboratório: 3 */
/* Estudante: Mariana Fernandes Cabral */
/* DRE: 121060838 */

class Pi {
   private double PiVet[];
   private double erro;

   public Pi (int n_threads){
   }

   public void initVet(int n_threads){
      PiVet = new double[n_threads];
      for (int k = 0; k<n_threads; k++) {
         PiVet[k] = 0 ;
      }
   }

   public void setPi(double valorPi, int idt){
      PiVet[idt] = valorPi;
   //   System.out.println(PiVet[idt]);
   }
   
   public double getPi(int idt){
      return PiVet[idt];
   }
   
   public void validarErroRelativo(double piObtido){
      this.erro = (Math.abs(Math.PI - piObtido))/(Math.PI);
      if(this.erro > Math.pow(10, -10)) {
         System.out.println("Erro relativo superior ao toleravel.\n");
         System.out.println(this.erro);
      //por alguma razão, minha comparação não está funcionando corretamente.
      }
   }
} 
class CalcularPi extends Thread {
   private int nthreads;
   private int tam;
   private int id;
   private double pithreads[];
   Pi pis;
   
   //--construtor
   public CalcularPi(int nthreads, int tam, int id, Pi pis) {
      this.nthreads = nthreads;
      this.tam = tam;
      this.id = id;
      this.pis = pis;
   }

   public void run() {
     pithreads = new double[nthreads];
     for (int j = id; j<tam; j+= nthreads) {
      pithreads[id] += (Math.pow(-1, j)*4.0) / (2 * j + 1);
     }
     System.out.println("Pi da thread " + id + " = " + pithreads[id]);
     System.out.println("Thread " + id + " executando.\n");
     this.pis.setPi(pithreads[id], id);  
   }

  public double getPi(int id) {
      return pithreads[id];
   }
}

//--classe do metodo main
class LeibnizPi {

   public static void main (String[] args) {
     int n_threads;
     int N;
     double pi = 0;
     /* recebe a quantidade de threads e o valor de N da fórmula de Leibniz 
      pela linha de comando.*/
     n_threads = Integer.parseInt(args[0]);
     N = Integer.parseInt(args[1]);
      //--reserva espaço para um vetor de threads
      Thread[] threads = new Thread[n_threads];
      
      Pi pis = new Pi(n_threads);
      pis.initVet(n_threads);
      //--PASSO 2: transformar o objeto Runnable em Thread
      for (int i=0; i<threads.length; i++) {
         final int qnt_threads = n_threads;
         threads[i] = new CalcularPi(qnt_threads, N, i, pis);
      }

      //iniciando as threads
      for (int i=0; i<threads.length; i++) {
         threads[i].start();
      }

      //esperando pelo termino das threads
      for (int i=0; i<threads.length; i++) {
            try { threads[i].join(); } 
            catch (InterruptedException e) { return; }
      } 

      for (int i=0; i<threads.length; i++) {
         pi += pis.getPi(i);
      //   System.out.println("\nSoma = " + pi);
      }
      System.out.println("\nPi = " + pi);
      //System.out.println("Terminou"); 
      pis.validarErroRelativo(pi);
   }
}