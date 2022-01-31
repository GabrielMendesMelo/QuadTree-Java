import java.awt.Graphics;
import java.awt.Rectangle;

public class SimulacaoNormal extends Cena {
    public SimulacaoNormal(Rectangle rect) {
        super(rect, "normal");
    }    

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(COR_FUNDO);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        for (Particula p : particulas) {
            p.paint(g);            
        }
    }

    @Override
    public void run() {
        while (rodando) {
            tempoProcessamento = System.nanoTime();

            for (int i = 0; i < particulas.size(); i++) {
                particulas.get(i).update();
                for (int j = 0; j < particulas.size(); j++) {
                    if (i == j) continue;
                    if (particulas.get(i).checarColisao(particulas.get(j))) {
                        particulas.get(i).onColisao();
                    }
                }
            }
            repaint();
            tempoProcessamento = System.nanoTime() - tempoProcessamento;
            totalTempoProcessamento += tempoProcessamento;
            qtdProcessos++;
            
            try {
                Thread.sleep(INTERVALO_THREAD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
