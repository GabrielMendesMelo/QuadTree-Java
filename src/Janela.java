import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Button;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class Janela extends JFrame {
    private static final int LARGURA = 660;
    private static final int ALTURA = 716;
    private Cena cena;

    //#region GUI
    private JPanel pnlGUI;

    private String tipoSimulacao;
    private String tipoInicio;

    private static final String
        //s = tipo de simulação
        sNORMAL = "NORMAL",
        sQUADTREE = "QUADTREE";

    private static final String
        //p == padrão de início
        pVAZIO = "VAZIO",
        pALEATORIO = "ALEATORIO",
        pEM_ORDEM = "EM_ORDEM",
        pMOLDURA = "MOLDURA",
        pX = "X";

    Button btnNormal = new Button("Normal");
    Button btnQuadTree = new Button("QuadTree");

    Button btnIniVazio = new Button("Manual");
    Button btnIniAleatorio = new Button("Aleatório");
    Button btnIniEmOrdem = new Button("Em ordem");
    Button btnIniMoldura = new Button("Moldura");
    Button bntIniX = new Button("X");
    JTextArea inputNParticulas = new JTextArea("N_PARTICULAS");

    Button btnComecar = new Button("Começar");
    Button btnFinalizar = new Button("Finalizar");
    //#endregion
    
    public Janela() {
        super("Objeto 5");
        criarJanela();
        pack();

        //#region GUI
        btnNormal.addActionListener(l -> {
            GUITipoSim(sNORMAL);
        });
        btnQuadTree.addActionListener(l -> {
            GUITipoSim(sQUADTREE);
        });
        btnIniVazio.addActionListener(l -> {
            GUITipoInicio(pVAZIO);
        });
        btnIniAleatorio.addActionListener(l -> {
            GUITipoInicio(pALEATORIO);
        });
        btnIniEmOrdem.addActionListener(l -> {
            GUITipoInicio(pEM_ORDEM);
        });
        btnIniMoldura.addActionListener(l -> {
            GUITipoInicio(pMOLDURA);
        });
        bntIniX.addActionListener(l -> {
            GUITipoInicio(pX);
        });
        btnComecar.addActionListener(l -> {
            GUIComecar();
        });
        btnFinalizar.addActionListener(l -> {
            GUIFinalizar();
        });
        //#endregion
    }

    private void criarJanela() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        criarGUI();
    }

    //#region GUI
    private void criarGUI() {
        pnlGUI = new JPanel();
        pnlGUI.setLayout(new FlowLayout(FlowLayout.LEFT));

        pnlGUI.add(btnNormal);
        pnlGUI.add(btnQuadTree);

        add(pnlGUI, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void GUILimpar() {
        pnlGUI.remove(btnNormal);
        pnlGUI.remove(btnQuadTree);
        pnlGUI.remove(btnIniVazio);
        pnlGUI.remove(btnIniAleatorio);
        pnlGUI.remove(btnIniEmOrdem);
        pnlGUI.remove(btnIniMoldura);
        pnlGUI.remove(bntIniX);
        pnlGUI.remove(inputNParticulas);
        pnlGUI.remove(btnComecar);
        pnlGUI.remove(btnFinalizar);
    }

    private void GUITipoSim(String tipoSimulacao) {
        this.tipoSimulacao = tipoSimulacao;

        GUILimpar();
        pnlGUI.add(btnIniVazio);
        pnlGUI.add(btnIniAleatorio);
        pnlGUI.add(btnIniEmOrdem);
        //pnlGUI.add(btnIniMoldura);
        //pnlGUI.add(bntIniX);
        pack();
    }

    private void GUITipoInicio(String padrao) {
        this.tipoInicio = padrao;

        GUILimpar();
        
        Rectangle rect = new Rectangle(0, 0, LARGURA - 20, ALTURA - 76);

        switch (tipoSimulacao) {
            case sNORMAL:
                cena = new SimulacaoNormal(rect);
                break;
            case sQUADTREE:
                cena = new SimulacaoQuadTree(rect);
                break;
        }

        int nParticulas = 0;
        switch(tipoInicio) {
            case pALEATORIO: case pEM_ORDEM:
                pnlGUI.add(inputNParticulas);
                break;
            case pMOLDURA:
                nParticulas = 100;
                cena.setNParticulas(nParticulas);
                cena.escolherPadrao(tipoInicio);
                break;
            case pX:
                nParticulas = 100;
                cena.setNParticulas(nParticulas);
                cena.escolherPadrao(tipoInicio);
                break;
            default:
                break;
        }
        
        pnlGUI.add(btnComecar);
        
        add(cena);
        pack();

        cena.liberarMouse();
        pack();
    }

    private void GUIComecar() {
        GUILimpar();
        if (tipoInicio == pALEATORIO ||
            tipoInicio == pEM_ORDEM ||
            tipoInicio == pMOLDURA) {

            cena.setNParticulas(Integer.parseInt(inputNParticulas.getText()));
            cena.escolherPadrao(tipoInicio);                
        }
        pnlGUI.add(btnFinalizar);
        pack();
        cena.comecar();
    }

    private void GUIFinalizar() {
        cena.finalizar();
    }
    //#endregion
}
