package org.devefx.mirror.common.xml;

import org.w3c.dom.Node;

public interface Nodelet {
	void process (Node node) throws Exception;
}
