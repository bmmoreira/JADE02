/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package metro;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class BookSellerGui extends JFrame {
	private metro.BookSellerAgent myAgent;
	private JTextField titleField;
	private JTextField priceField;

	BookSellerGui(BookSellerAgent a) {
		super(a.getLocalName());
		this.myAgent = a;
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(new JLabel("Book title:"));
		this.titleField = new JTextField(15);
		p.add(this.titleField);
		p.add(new JLabel("Price:"));
		this.priceField = new JTextField(15);
		p.add(this.priceField);
		this.getContentPane().add(p, "Center");
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String title = metro.BookSellerGui.this.titleField.getText().trim();
					String price = metro.BookSellerGui.this.priceField.getText().trim();
					metro.BookSellerGui.this.myAgent.updateCatalogue(title, Integer.parseInt(price));
					metro.BookSellerGui.this.titleField.setText("");
					metro.BookSellerGui.this.priceField.setText("");
				} catch (Exception var4) {
					JOptionPane.showMessageDialog(metro.BookSellerGui.this, "Invalid values. " + var4.getMessage(), "Error", 0);
				}

			}
		});
		p = new JPanel();
		p.add(addButton);
		this.getContentPane().add(p, "South");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				metro.BookSellerGui.this.myAgent.doDelete();
			}
		});
		this.setResizable(false);
	}

	public void showGui() {
		this.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		this.setLocation(centerX - this.getWidth() / 2, centerY - this.getHeight() / 2);
		super.setVisible(true);
	}
}
