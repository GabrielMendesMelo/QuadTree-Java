public class EscreverMedia implements Runnable {
    private volatile boolean rodando = false;
    private static final int INTERVALO_ESCREVER_NO_ARQUIVO = 1000;
    private int id = 0;

    private Cena cena;
    private Thread t;

    public EscreverMedia(Cena cena) {
        this.cena = cena;
        t = new Thread(this);
    }

    public void comecar() {
        t.start();
        rodando = true;
    }
    
    public void parar() {
        rodando = false;
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while (rodando) {   
            cena.escreverMedia(id++);
            
            try {
                Thread.sleep(INTERVALO_ESCREVER_NO_ARQUIVO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }        
    }
}
