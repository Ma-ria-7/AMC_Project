
package Projeto;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.System.Logger;



import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Toolkit;

public class Window {
    
    private MRFT[] MRFs = null;
    private double[] prob = null;
    private Classifier c = null;
    private Dataset d = null;
    private String path = null;
    boolean clicked = false;
    private JFrame frame;
    private JTextField textField;
    private JTextField textField_2;
    private JTextField textField_1;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Window window = new Window();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Window() {
        initialize();
    }

    private void exportClassifier(Classifier classifier, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(classifier);
            textField.setText("Classificador exportado com sucesso");
            textField.setForeground(new Color(0, 100, 0));
        } catch (IOException e) {
            textField.setText("Erro ao exportar classificador: " + e.getMessage());
            textField.setForeground(Color.RED);
        }
    }

    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\maria\\OneDrive\\Imagens\\Capturas de Ecrã\\Captura de ecrã 2025-01-12 225447.png"));
        frame.setBounds(200, 200, 900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle("Classificador de Dados");

        JTextArea textArea = new JTextArea();
        textArea.setToolTipText("Insira os valores a classificar separados por vírgulas");
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(166, 306, 562, 50);
        frame.getContentPane().add(scrollPane);

        JButton btnNewButton = new JButton("Classificar");
        btnNewButton.setBackground(Color.LIGHT_GRAY);
        btnNewButton.setFont(new Font("Eras Medium ITC", Font.BOLD, 14));
        btnNewButton.setToolTipText("Clique para classificar os valores inseridos");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputText = textArea.getText();
                textField.setForeground(Color.BLACK); // Reset text color to black

                if (c == null) {
                    textField.setText("Por favor, treine o classificador ou importe um classificador");
                    textField.setForeground(Color.RED);
                    return;
                }

                int expectedNumValues = c.getNumColunas() - 1;
                if (inputText.isEmpty()) {
                    textField.setText("Por favor, insira valores para classificar");
                    textField.setForeground(Color.RED);
                } else if (inputText.split(",").length != expectedNumValues) {
                    textField.setText("Número de valores inválido, número de valores esperado: " + expectedNumValues);
                    textField.setForeground(Color.RED);
                } else {
                    try {
                        int[] convertedVector = Dataset.convert(inputText);
                        boolean outOfBounds = false;
                        for (int i = 0; i < convertedVector.length; i++) {
                            int min = c.min(i);
                            int max = c.max(i);
                            int value = convertedVector[i];
                        
                            if (value < min || value > max) {
                                outOfBounds = true;
                                break;
                            }
                        }
                        if (outOfBounds) {
                            textField.setText("Valores fora do limite de variáveis");
                            textField.setForeground(Color.RED);
                        } else {
                            int classification = c.classify(convertedVector);
                            textField.setText("Classe mais provável: " + classification);
                            textField.setForeground(new Color(0, 100, 0));
                        }
                    } catch (Exception ex) {
                        
                        textField.setText("Erro ao processar os dados");
                        textField.setForeground(Color.RED);
                    }
                }
            }
        });

        btnNewButton.setBounds(380, 217, 126, 21);
        frame.getContentPane().add(btnNewButton);

        textField = new JTextField();
        textField.setBounds(166, 391, 562, 80);
        textField.setEditable(false);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnNewButton_2 = new JButton("Accuracy");
        btnNewButton_2.setBackground(Color.LIGHT_GRAY);
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textField.setForeground(Color.BLACK); // Reset text color to black

                if (path == null) {
                    textField.setText("Por favor, escolha um dataset");
                    textField.setForeground(Color.RED);
                    return;
                }
                
                // Desabilitar o botão durante o cálculo
                btnNewButton_2.setEnabled(false);
                textField.setText("Calculando accuracy... Por favor aguarde.");
                
                // Usar SwingWorker para não bloquear a interface
                SwingWorker<Double, Void> worker = new SwingWorker<Double, Void>() {
                    @Override
                    protected Double doInBackground() throws Exception {
                        return Algoritmo.accuracy(d);
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            double accuracy = get();
                            textField_1.setText(String.format("%.2f%%", accuracy));//Mostra a precisão com duas casas decimais
                            textField.setText("Cálculo concluído!");
                            textField.setForeground(new Color(0, 100, 0));
                        } catch (Exception ex) {
                            textField.setText("Erro no cálculo: " + ex.getMessage());
                            textField.setForeground(Color.RED);
                        } finally {
                            btnNewButton_2.setEnabled(true);
                        }
                    }
                };
                
                worker.execute();
            }
        });
        btnNewButton_2.setBounds(567, 32, 161, 21);
        frame.getContentPane().add(btnNewButton_2);
        
        
        JButton btnNewButton_1 = new JButton("Importar Classificador");
        btnNewButton_1.setBackground(Color.LIGHT_GRAY);
        btnNewButton_1.setToolTipText("Escolher um ficheiro .ser para funcionar como classificador");

btnNewButton_1.addActionListener(e -> {
    JFileChooser fc = new JFileChooser();
    int returnVal = fc.showOpenDialog(btnNewButton_1);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "O ficheiro selecionado não existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (file.getName().contains(".ser")) {
            try (FileInputStream fi = new FileInputStream(file.getPath());
                 ObjectInputStream oi = new ObjectInputStream(fi)) {
                c = (Classifier) oi.readObject();
                d = null; // Apagar o dataset existente
                textField_2.setText(""); // Limpar o campo do dataset
                textField.setText("Classificador carregado de \n" + file.getPath());
                textField.setForeground(new Color(0, 100, 0));
                btnNewButton_2.setEnabled(false); // Desabilitar o botão "Accuracy"

                // Check for values outside the limits using getMaxs and getMins
                int[] maxValues = c.getMaxs();
                int[] minValues = c.getMins();
                String inputText = textArea.getText();

                if (!inputText.isEmpty()) {
                    int[] convertedVector = Dataset.convert(inputText);
                    for (int i = 0; i < convertedVector.length; i++) {
                        if (convertedVector[i] < minValues[i] || convertedVector[i] > maxValues[i]) {
                            // Do nothing if out of bounds
                            break;
                        }
                    }
                }

                textField.setText("Classificador importado com sucesso");
                textField.setForeground(new Color(0, 100, 0));
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
                textField.setText("Erro ao carregar o classificador: " + e1.getMessage() + "\n");
                textField.setForeground(Color.RED);
            }
        } else {
            textField.setText("Ficheiro não suportado.\n");
            textField.setForeground(Color.RED);
        }
    }
});



        btnNewButton_1.setBounds(567, 92, 161, 21);
        frame.getContentPane().add(btnNewButton_1);

        textField_2 = new JTextField();
        textField_2.setBounds(207, 33, 181, 19);
        textField_2.setEditable(false);
        frame.getContentPane().add(textField_2);
        textField_2.setColumns(10);

        Label label = new Label("Colocar valores");
        label.setFont(new Font("Dialog", Font.BOLD, 10));
        label.setBounds(400, 279, 118, 21);
        frame.getContentPane().add(label);

        

        textField_1 = new JTextField();
        textField_1.setBounds(567, 63, 161, 19);
        textField_1.setEditable(false);
        frame.getContentPane().add(textField_1);
        textField_1.setColumns(10);

        JButton btnNewButton_3 = new JButton("Treinar Classificador");
        btnNewButton_3.setBackground(Color.LIGHT_GRAY);
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textField.setForeground(Color.BLACK); // Reset text color to black
                if (path == null) {
                    textField.setText("Por favor, escolha um dataset");
                    textField.setForeground(Color.RED);
                } else {
                    MRFs = Chow_Liu.MRFsetup(d);
                    prob = Algoritmo.c_prob(d);
                    c = new Classifier(MRFs, prob, d.getMaxs(), d.getMins());
                    textField.setText("Classificador treinado com sucesso");
                    textField.setForeground(new Color(0, 100, 0));
                }
            }
        });
        btnNewButton_3.setBounds(36, 62, 163, 21);
        frame.getContentPane().add(btnNewButton_3);

        JButton btnNewButton_4 = new JButton("Exportar Classificador");
        btnNewButton_4.setBackground(Color.LIGHT_GRAY);
        btnNewButton_4.setToolTipText("Clique para exportar o classificador para uma pasta no desktop");
        btnNewButton_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setForeground(Color.BLACK); // Reset text color to black}
                if (c == null) {
                    textField.setText("Por favor treine um classificador antes de exportar.\n");
                    textField.setForeground(Color.RED);
                    return;
                }
                try {
                    String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
                    File exportDir = new File(desktopPath, "classificadores_treinados");
                    if (!exportDir.exists()) {
                        exportDir.mkdirs();
                    }
                    String datasetName = new File(path).getName().replaceFirst("[.][^.]+$", "");
                    String filePath = exportDir.getAbsolutePath() + File.separator + "classificador_" + datasetName + ".ser";
                    File exportFile = new File(filePath);
                    if (exportFile.exists()) {
                        textField.setText("Já existe um classificador para este dataset: " + filePath);
                    } else {
                        exportClassifier(c, filePath);
                        textField.setText("Classificador exportado para: " + filePath);
                       
                        textField.setForeground(new Color(0, 100, 0));
                    }
                } catch (Exception ex) {
                    textField.setText("Erro ao exportar classificador: " + ex.getMessage());
                    textField.setForeground(Color.RED);
                }
            }
        });
        btnNewButton_4.setBounds(36, 93, 163, 21);
        frame.getContentPane().add(btnNewButton_4);

        JButton btnNewButton_5 = new JButton("Escolher Dataset");
        btnNewButton_5.setBackground(Color.LIGHT_GRAY);
        btnNewButton_5.setToolTipText("Escolher um ficheiro .csv para funcionar como dataset");
        btnNewButton_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (c != null) {
                    int response = JOptionPane.showConfirmDialog(frame,
                        "Um classificador já está treinado ou importado. Deseja continuar? Isso apagará a memória de todos os classificadores.",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    if (response != JOptionPane.YES_OPTION) {
                        return;
                    }
                    c = null; // Reset the classifier
                    textField.setText(""); // Clear textField_2
                    btnNewButton_2.setEnabled(true); // Enable the "Accuracy" button
                }

                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    path = selectedFile.getAbsolutePath();
                    if (!selectedFile.exists()) {
                        JOptionPane.showMessageDialog(frame, "O ficheiro selecionado não existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                        textField_2.setText("");
                    } else if (path.endsWith(".csv")) {
                        d = new Dataset(path);
                        textField_2.setText(selectedFile.getName());
                    } else {
                        textField_2.setText("Por favor, escolha um ficheiro .csv");
                    }
                }
            }
        });
        btnNewButton_5.setBounds(34, 32, 163, 21);
        frame.getContentPane().add(btnNewButton_5);
    }
}






