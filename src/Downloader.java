import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Downloader extends JFrame
{
    JPanel text = new JPanel();
    JProgressBar progressBar = new JProgressBar(0, 100);
    JLabel label = new JLabel();
    public Downloader()
    {
        //setSize(1000, 200);
        UIManager.LookAndFeelInfo looks[];
        looks = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(looks[1].getClassName());
        } catch (Exception e) {
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        text.setLayout(new BorderLayout());
        text.add(new JLabel("Enter URL: "), BorderLayout.WEST);
        JTextField url = new JTextField(20);
        text.add(url, BorderLayout.CENTER);
        JButton download = new JButton("Download");
        text.add(download, BorderLayout.EAST);
        label.setText(" ");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(true);
        add(text);
        add(progressBar);
        add(label);
        pack();
        setVisible(true);

        download.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Descargando...");
                progressBar.setValue(0);
                download.setEnabled(false);
                String urlText = url.getText();
                if (urlText == null || urlText.isEmpty()) {
                    mensajeError("URL no puede estar vacía");
                    download.setEnabled(true);
                    return;
                }

                try {
                    URL downloadUrl = new URL(urlText);
                    URLConnection connection = downloadUrl.openConnection();
                    int fileSize = connection.getContentLength();
                    String archivo = "descarga" + getExtension(urlText);

                    progressBar.setMaximum(fileSize);

                    try (InputStream input = new BufferedInputStream(downloadUrl.openStream());
                         OutputStream output = new FileOutputStream(archivo)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        int totalBytesRead = 0;
                        while ((bytesRead = input.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                            progressBar.setValue(totalBytesRead);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    mensajeError("Ingrese URL valida");
                    download.setEnabled(true);
                    return;
                }
                label.setText("Descarga Completada");
                download.setEnabled(true);
            }
        });

    }

    public String getExtension(String url) {
        String extension = "";
        if (url != null) {
            int ultimoPunto = url.lastIndexOf('.');
            if (ultimoPunto > 0) {
                extension = url.substring(ultimoPunto+ 1);
            }
        }
        return extension.toLowerCase();
    }

    public void mensajeError(String message) {
        String alertEmoji = "\uD83D\uDCA3";
        JOptionPane.showMessageDialog(null, alertEmoji + " " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
