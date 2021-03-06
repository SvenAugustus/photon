/*
 * MIT License
 *
 * Copyright (c) 2020 SvenAugustus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.flysium.photon.chat;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import xyz.flysium.photon.Constant;

/**
 * Chat Server Frame
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class ChatServerFrame extends JFrame {

  public static void main(String[] args) throws InterruptedException {
    ChatServerFrame serverFrame = new ChatServerFrame(new ChatServer(Constant.PORT));
    serverFrame.start();
  }

  private final ChatServer server;
  private final Thread readFromConsole;

  private final JTextArea console = new JTextArea();

  public ChatServerFrame(final ChatServer server) throws HeadlessException {
    this.server = server;
    this.readFromConsole = new Thread(new UpdateConsoleRunnable(), "readFromConsole");

    this.console.setEditable(false);
    this.console.setLineWrap(true);
    this.console.setWrapStyleWord(true);
    JScrollPane scroll = new JScrollPane(this.console);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.add(scroll, BorderLayout.CENTER);

    this.setTitle("Chat Server");
    this.setSize(800, 600);
    this.setLocation(500, 150);
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }

  /**
   * Start
   */
  public void start() throws InterruptedException {
    this.setVisible(true);
    this.readFromConsole.start();
    this.server.start();
  }

  /**
   * update Console.
   *
   * @param text append String
   */
  private void updateConsole(String text) {
    if (text == null) {
      return;
    }
    this.console.append(System.lineSeparator());
    this.console.append(text);
  }

  class UpdateConsoleRunnable implements Runnable {

    @Override
    public void run() {
      while (true) {
        try {
          TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException exc) {
          exc.printStackTrace();
        }
        try {
          String readiedString = ChatServerFrame.this.server.readConsole();
          if (readiedString != null) {
            // update Console.
            updateConsole(readiedString);
          }
        } catch (InterruptedException exec) {
          exec.printStackTrace();
        }
      }
    }
  }

}
