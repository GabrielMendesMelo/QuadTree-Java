import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class Cena extends JPanel implements Runnable, MouseInputListener {
    protected volatile boolean rodando = false;
    protected static final int INTERVALO_THREAD = 16;

    protected static final Color COR_CONTORNO = Color.decode("#84728C");
    protected static final Color COR_FUNDO = Color.decode("#66546E");
    protected static final Color[] CORES_CIRCULO = new Color[8];
    private static final int PARTICULA_DIMENSAO = 3;
    private static final int MAX_VELOCIDADE = 3;

    protected static final ArrayList<Particula> particulas = new ArrayList<>();

    protected Rectangle rect;
    protected int nParticulas;

    protected long tempoProcessamento = 0;
    protected long totalTempoProcessamento = 0;
    protected long qtdProcessos = 0;
    private String nomeArquivo;
    private EscreverMedia escreverMedia;

    protected Thread loop;

    public Cena(Rectangle rect, String nomeArquivo) {
        this.rect = rect;
        this.nomeArquivo = nomeArquivo;
        
        CORES_CIRCULO[0] = Color.BLUE;
        CORES_CIRCULO[1] = Color.CYAN;
        CORES_CIRCULO[2] = Color.GREEN;
        CORES_CIRCULO[3] = Color.ORANGE;
        CORES_CIRCULO[4] = Color.PINK;
        CORES_CIRCULO[5] = Color.RED;
        CORES_CIRCULO[6] = Color.YELLOW;

        loop = new Thread(this);
    }

    public void escolherPadrao(String padrao) {
        int x = 0;
        int y = 0;
        int velX = 1;
        int velY = 1;

        Rectangle particulaRect;

        int l = rect.width;
        int a = rect.height;
        int d = PARTICULA_DIMENSAO;

        for (int i = 0; i < nParticulas; i++) {
            Color cor = CORES_CIRCULO[ThreadLocalRandom.current().nextInt(0, CORES_CIRCULO.length)];
            switch(padrao) {
                case "ALEATORIO":
                    x = ThreadLocalRandom.current().nextInt(0, rect.width - d);
                    y = ThreadLocalRandom.current().nextInt(0, rect.height - d);
                    velX = ThreadLocalRandom.current().nextInt(1, MAX_VELOCIDADE);
                    velY = ThreadLocalRandom.current().nextInt(1, MAX_VELOCIDADE);

                    particulaRect = new Rectangle(x, y, d, d);
                    particulas.add(new Particula(particulaRect, rect.width, rect.height, velX, velY, cor, true));
                    break;
                case "EM_ORDEM":
                    int maxPorLinha = l / (d * 4) + 1;
                    int maxPorColuna = a / (d * 2);
                    int max = maxPorLinha * maxPorColuna + 1;

                    if (i == max) {
                        nParticulas = max;
                        return;
                    }

                    if (i < maxPorLinha) {
                        x = i * d * 4 + d;
                        y = 0;
                    } else {
                        x = (i - (maxPorLinha * (i / maxPorLinha))) * d * 4 + d;
                        y = i / maxPorLinha * d * 2 + d;
                    }

                    particulaRect = new Rectangle(x, y, d, d);
                    particulas.add(new Particula(particulaRect, rect.width, rect.height, velX, velY, cor));
                    break;
                case "X":
                    //NÃO IMPLEMENTADO
                    break;
                case "MOLDURA":
                    //NÃO IMPLEMENTADO
                    break;
                default:
                    System.out.print("NÃO IMPLEMENTADO!");
                    return;
            }
        }
    }

    public void liberarMouse() {
        addMouseListener(this);
    }

    public void comecar() {
        rodando = true;
        loop.start();
        escreverMedia = new EscreverMedia(this);
        escreverMedia.comecar();
    }
    
    public void finalizar() {
        escreverMedia.parar();
        rodando = false;
        escreverMedia(-1);
        try {
            loop.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
    
    public void escreverMedia(int id) {
        if (qtdProcessos == 0) return;

        Path path = Path.of("src/resultado/" + nomeArquivo + ".txt");
        String media = (id == -1 ? "Média final" : "[" + id + "]") + " n=" + nParticulas + ": " + (totalTempoProcessamento / qtdProcessos) + "ns";

        if (Files.isReadable(path)) {
            try {
                String txt = Files.readString(path);
                if (txt == "") {
                    txt = media;
                } else {
                    txt = txt + "\n" + media;
                }
                Files.writeString(path, txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Não foi possível escrever no arquivo.");
        }
    }

    @Override
    public void run() {}
    
    @Override
    public void mousePressed(MouseEvent e) {
        Rectangle pRect = new Rectangle(e.getX() - PARTICULA_DIMENSAO / 2, e.getY() - PARTICULA_DIMENSAO / 2, PARTICULA_DIMENSAO, PARTICULA_DIMENSAO);
        int velX = ThreadLocalRandom.current().nextInt(1, MAX_VELOCIDADE);
        int velY = ThreadLocalRandom.current().nextInt(1, MAX_VELOCIDADE);
        particulas.add(new Particula(pRect, rect.width, rect.height, velX, velY, CORES_CIRCULO[ThreadLocalRandom.current().nextInt(0, CORES_CIRCULO.length)], true));
        nParticulas++;
        repaint();
    }

    //#region MouseInputListener não usados
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
    //#endregion
    
    public void setNParticulas(int nParticulas) {
        this.nParticulas = nParticulas;
    }
}
