/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package chibi.gumtreediff.gen.jdt;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import chibi.gumtreediff.gen.jdt.cd.EntityType;
import chibi.gumtreediff.tree.ITree;
import chibi.gumtreediff.tree.TreeContext;
import chibi.gumtreediff.tree.TypeLabel;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;

public abstract class AbstractJdtVisitor extends ASTVisitor {

    protected TreeContext context = new TreeContext();

    private Deque<ITree> trees = new ArrayDeque<>();
    
    private CompilationUnit treeRoot;

    public AbstractJdtVisitor() {
        super(true);
    }

    public TreeContext getTreeContext() {
        return context;
    }

    protected void pushNode(ASTNode n, String label) {
        int type = n.getNodeType();
        String typeName = n.getClass().getSimpleName();
        TypeLabel.put(type,typeName);
        
        if (n.getNodeType() == 32) { //MethodInvocation
        	push(type, typeName, label, n.getStartPosition(), n.getLength(), treeRoot.getLineNumber(n.getStartPosition())-1, ((MethodInvocation)n).getName().toString(), ((MethodInvocation)n).arguments());
		}
        else{
        	push(type, typeName, label, n.getStartPosition(), n.getLength(), treeRoot.getLineNumber(n.getStartPosition())-1);
        }        
    }

    protected void pushFakeNode(EntityType n, int startPosition, int length) {
        int type = -n.ordinal(); // Fake types have negative types (but does it matter ?)
        String typeName = n.name();
        push(type, typeName, "", startPosition, length, -1);
    }

    private void push(int type, String typeName, String label, int startPosition, int length, int lineNumber) {
    	
        ITree t = context.createTree(type, label, typeName);
        t.setPos(startPosition);
        t.setLength(length);
        t.setLineNumber(lineNumber);

        if (trees.isEmpty())
            context.setRoot(t);
        else {
            ITree parent = trees.peek();
            t.setParentAndUpdateChildren(parent);
        }

        trees.push(t);
    }
    
    private void push(int type, String typeName, String label, int startPosition, int length, int lineNumber, String methodName, List arguments) {
    	
        ITree t = context.createTreeMethodInvocation(type, label, typeName, methodName, arguments);
        t.setPos(startPosition);
        t.setLength(length);
        t.setLineNumber(lineNumber);

        if (trees.isEmpty())
            context.setRoot(t);
        else {
            ITree parent = trees.peek();
            t.setParentAndUpdateChildren(parent);
        }

        trees.push(t);
    }

    protected ITree getCurrentParent() {
        return trees.peek();
    }

    protected void popNode() {
        trees.pop();
    }

	public CompilationUnit getTreeRoot() {
		return treeRoot;
	}

	public void setTreeRoot(CompilationUnit treeRoot) {
		this.treeRoot = treeRoot;
	}
}
