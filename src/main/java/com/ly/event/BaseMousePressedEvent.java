package com.ly.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 鼠标监听，只提供mousePressed鼠标点击方法，其他方法空实现
 * @author luoyoujun
 *
 */
public abstract class BaseMousePressedEvent  implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
