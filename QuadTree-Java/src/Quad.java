import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Quad extends Rectangle {
    public Quad(Rectangle rect) {
        super(rect);
    }

    public boolean contem(Particula p) {
        return (p.x + p.width / 2 >= x && p.x + p.width / 2 <= x + width && p.y + p.height / 2 >= y && p.y + p.height / 2 <= y + height);
    }

    public void paint(Graphics g, Color corFundo, Color corContorno) {
        g.setColor(corFundo);
        g.fillRect(x, y, width, height);
        g.setColor(corContorno);
        g.drawRect(x, y, width, height);
    }

    public void update(ArrayList<Particula> particulas) {
        for (int i = particulas.size() - 1; i > -1; i--) {
            for (int j = 0; j < particulas.size(); j++) {
                if (i == j) continue;
                if (particulas.get(i).checarColisao(particulas.get(j))) particulas.get(i).onColisao();
            }
        }
    }
}
