import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Color;
import java.awt.Graphics;

public class Particula extends Rectangle {
    private final int telaLargura, telaAltura;
    private int velX, velY;
    private Color cor;
    private Color corOriginal;

    private boolean colidiu = false;
    private int timerCor = 0;
    private static final int TIMER_COR_MAX = 10;

    public Particula(Rectangle rect, int telaLargura, int telaAltura, int velX, int velY, Color cor) {
        super(rect);

        this.telaLargura = telaLargura;
        this.telaAltura = telaAltura;
        this.velX = velX;
        this.velY = velY;
        
        this.cor = cor;
        this.corOriginal = cor;
    }

    public Particula(Rectangle rect, int telaLargura, int telaAltura, int velX, int velY, Color cor, boolean aleatorio) {
        super(rect);

        this.telaLargura = telaLargura;
        this.telaAltura = telaAltura;
        this.velX = velX;
        this.velY = velY;
        
        this.cor = cor;
        this.corOriginal = cor;

        int rand = ThreadLocalRandom.current().nextInt(0, 4);
        
        if (rand == 0) {
            this.velX *= -1;
        } else if (rand == 1) {
            this.velX *= -1;
            this.velY *= -1;
        } else if (rand == 2) {
            this.velY *= -1;
        }
    }

    private void mover() {
        setLocation(x + velX, y + velY);
        if (x < 0) {
            x = 0;
            velX *= -1;
        }
        if (y < 0) {
            y = 0;
            velY *= -1;
        }
        if (x > telaLargura - width) {
            x = telaLargura - width;
            velX *= -1;
        }
        if (y > telaAltura - height) {
            y = telaAltura - height;
            velY *= -1;
        }
    }

    public boolean checarColisao(Particula outro) {
        if (((x <= outro.x && x + width >= outro.x) || 
            (x >= outro.x && x <= outro.x + outro.width)) && 
            ((y <= outro.y && y + height >= outro.y) || 
            (y >= outro.y && y <= outro.y + outro.height))) {
                
            colidiu = true;
        }
        return colidiu;
    }

    public void onColisao() {
        cor = Color.WHITE;
    }

    public void paint(Graphics g) {        
        g.setColor(cor);
        g.fillOval(x, y, width, height);
        g.drawOval(x, y, width, height);
    }

    public void update() {
        mover();

        if (colidiu) {
            timerCor++;
            if (timerCor > TIMER_COR_MAX) {
                timerCor = 0;
                cor = corOriginal;
                colidiu = false;
            }
        }
    }
}
