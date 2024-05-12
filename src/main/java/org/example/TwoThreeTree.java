package org.example;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class TwoThreeTree {
    private int counter = 0;
    private double time = 0;;
    private int size;
    private TreeNode root;
    private boolean successfulInsertion;
    private boolean successfulDeletion;
    private boolean split;
    private boolean underflow;
    private boolean first;
    private boolean singleNodeUnderflow;

    private enum Nodes {
        LEFT, MIDDLE, RIGHT, DUMMY;
    }
    public TwoThreeTree() {
        size = 0;
        root = null;
        successfulInsertion = false;
        successfulDeletion = false;
        underflow = false;
        singleNodeUnderflow = false;
        split = false;
        first = false;
    }
    private class Node {

    }
    private class TreeNode extends Node {
        int keys[];
        Node children[];
        int degree;
        TreeNode() {
            keys = new int[2];
            children = new Node[3];
            degree = 0;
        }

        void print() {

            if (degree == 1) {
                System.out.print("(-,-)");
            } else if (degree == 2) {
                System.out.print("(" + keys[0] + ",-) ");
            } else {
                System.out.print("(" + keys[0] + "," + keys[1] + ") ");
            }
        }
    }
    private class LeafNode extends Node {

        int key;

        LeafNode(int key) {
            this.key = key;
        }
        void print() {
            System.out.print(key + " ");
        }
    }

    private void insertKey(int key) {
        Node[] array = new Node[2]; // insert возвращает array из 2 нодов: обновленного и ранее созданного
        array = insert(key, root);
        if (array[1] == null) { //если доп нодов не создавалось-обновляем рут напрямую
            root = (TreeNode) array[0];
        } else {
            //иначе произошло разделение и нужно создать и добавить еще один узел,
            // который бы объединил их
            TreeNode treeRoot = new TreeNode();
            treeRoot.children[0] = array[0];
            treeRoot.children[1] = array[1];
            updateTree(treeRoot);
            counter++;
            root = treeRoot;
        }
    }
    private Node[] insert(int key, Node n) {
        counter ++; // каждый вызов insert сопровождается увеличением counter;
        Node array[] = new Node[2];
        Node catchArray[] = new Node[2]; //хранит обновленнные ноды
        TreeNode t = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }
        if (root == null && !first) { //наша нода первая
            first = true;
            TreeNode newNode = new TreeNode();
            t = newNode;

            t.children[0] = insert(key, t.children[0])[0];
            updateTree(t);
            array[0] = t;
            array[1] = null;
        }
        else if (t != null && !(t.children[0] instanceof LeafNode)) { // если узел не ссылается на листья
            // и сам не является листом
            if (key < t.keys[0]) {
                // если вставляемый элемент меньше первого ключа узла
                catchArray = insert(key, t.children[0]);
                // вызов вставки на левое поддерево
                t.children[0] = catchArray[0];
                if (split) { // если разделение произошло
                    if (t.degree <= 2) {
                        //если у узла не более 2-х детей
                        split = false;
                        t.children[2] = t.children[1];
                        t.children[1] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else if (t.degree > 2) { //если у узла более 2-х детей
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[1] = catchArray[1];
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                        // newNode- нод который добавляется для осуществления разделения при более чем 2-х ключах
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
            // больше или равен первому ключу и второй или отсуствует или меньше второго ключа
            else if (key >= t.keys[0] && (t.children[2] == null || key < t.keys[1])) {
                catchArray = insert(key, t.children[1]);
                t.children[1] = catchArray[0];
                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
            //если наш ключ больше второго
            else if (key >= t.keys[1]) {
                catchArray = insert(key, t.children[2]);
                t.children[2] = catchArray[0];
                if (split) {
                    if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[0];
                        newNode.children[1] = catchArray[1];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
        }
        //узел срединный чьи потомки листы
        else if (t != null && t.children[0] instanceof LeafNode) {
            //получаем ссылки на детей
            LeafNode l1 = null, l2 = null, l3 = null;
            if (t.children[0] != null && t.children[0] instanceof LeafNode) {
                l1 = (LeafNode) t.children[0];
            }
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            //если у узла не более 2-х детей
            //просто куда бы вставить....
            if (t.degree <= 2) {
                if (t.degree == 1 && key > l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[1] = leaf;
                } else if (t.degree == 1 && key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = l2;
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key < l2.key && key > l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = l2;
                    t.children[1] = leaf;
                } else if (t.degree == 2) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = leaf;
                }
                updateTree(t);
                array[0] = t;
                array[1] = null;
            }
            else if (t.degree > 2) {
                split = true;
                if (key < l1.key) {
                    // просто происходит процедура разделения
                    // нода с детьми на два новых
                    LeafNode leaf = new LeafNode(key);
                    TreeNode newNode = new TreeNode();
                    t.children[0] = leaf;
                    t.children[1] = l1;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l1.key && key < l2.key) {
                    LeafNode leaf = new LeafNode(key);
                    TreeNode newNode = new TreeNode();
                    t.children[1] = leaf;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l2.key && key < l3.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = leaf;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l3.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = l3;
                    newNode.children[1] = leaf;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                }
            }
            successfulInsertion = true;
        } else if (n == null) {
            successfulInsertion = true;
            array[0] = new LeafNode(key);
            array[1] = null;
            return array;
        }
        return array;
    }
    private Node remove(int key, Node n) {
        counter ++;
        TreeNode t = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }
        if (n == null) {
            return null;
        }
        //если узел-не лист,значит мы не достигли нужного узла
        //поскольку удаляется элемент в деревьях из листьев
        if (t != null && t.children[0] instanceof TreeNode) {
            if (key < t.keys[0]) {
                t.children[0] = remove(key, t.children[0]);
                if (singleNodeUnderflow) { // после удаления появился узел с одним дитятком
                    TreeNode child = (TreeNode) t.children[0];
                    TreeNode rightChild = (TreeNode) t.children[1];
                    //получаем детей

                    if (rightChild.degree == 2) { // если у правого ребенка два наследника
                        //присоединяем child к правому и обновляем деревл
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        t.children[0] = rightChild;
                        t.children[1] = t.children[2];
                        t.children[2] = null;
                        if (t.degree == 2) {
                            //при удалении из узла с двумя детьми
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    }
                    // если же степень правого 3,то
                    // создаем новый узел и
                    // присоединяем  к нему наследника правого и наследника узла с одним ребенеком
                    else if (rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[0];
                        newNode.children[1] = rightChild.children[0];
                        t.children[0] = newNode;
                        updateTree(newNode);
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                }
                else if (underflow) {
                    underflow = false;
                    TreeNode child = (TreeNode) t.children[0];
                    TreeNode rightChild = (TreeNode) t.children[1];
                    if (rightChild.degree == 3) {
                        //если у правого наследника 3 наследника то берем кранейго левого и присоединяем
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    }
                    else if (rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        //присоединяем единственного наследника к правому
                        t.children[0] = rightChild;
                        if (t.degree == 3) {
                            t.children[1] = t.children[2];
                            t.children[2] = null;
                        }
                        else {
                            Node ref = t.children[0];
                            t = (TreeNode) ref;
                            singleNodeUnderflow = true;
                        }
                    }
                }
                updateTree(t);
            }
            // в середине или справа от одного ключа
            else if (key >= t.keys[0] && (t.children[2] == null || key < t.keys[1])) {
                t.children[1] = remove(key, t.children[1]);
                if (singleNodeUnderflow) {
                    TreeNode leftChild = (TreeNode) t.children[0];
                    TreeNode child = (TreeNode) t.children[1];
                    TreeNode rightChild = (TreeNode) t.children[2];
                    if (leftChild.degree == 2) {
                        // если у левого наследника 2 наследника то берем "одинокий" узел-наслденик и присоединяем к нему
                        leftChild.children[2] = child;
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        updateTree(leftChild);
                        if (t.degree == 2) {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    }
                    //иначе то же самое для правого
                    else if (rightChild != null && rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        singleNodeUnderflow = false;
                    }
                    else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = child;
                        t.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                        singleNodeUnderflow = false;
                    }
                    else if (rightChild != null && rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = child;
                        newNode.children[1] = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        t.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                }
                else if (underflow) {
                    underflow = false;
                    // получаем узлы-наследники
                    TreeNode leftChild = (TreeNode) t.children[0];
                    TreeNode child = (TreeNode) t.children[1];
                    TreeNode rightChild = (TreeNode) t.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    }
                    else if (rightChild != null && rightChild.degree == 3) {
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    }
                    else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        t.children[1] = null;
                        if (t.degree == 3) {
                            // если узел степени 3,то underflow не произойдет
                            t.children[1] = t.children[2];
                            t.children[2] = null;
                        }
                        else {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        }
                    }
                    else if (rightChild != null && rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        singleNodeUnderflow = false;
                    }
                }
                updateTree(t);
            }
            else if (key >= t.keys[1]) { // повторяем то же самое с той разнницей что leftchild - срединный наследник
                t.children[2] = remove(key, t.children[2]);
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) t.children[2];
                    TreeNode leftChild = (TreeNode) t.children[1];
                    if (leftChild.degree == 2) {
                        leftChild.children[2] = child;
                        t.children[2] = null;
                        updateTree(leftChild);
                    }
                    else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = t.children[2];
                        t.children[2] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                    }
                    singleNodeUnderflow = false;
                }
                else if (underflow) {

                    underflow = false;

                    TreeNode leftChild = (TreeNode) t.children[1];
                    TreeNode child = (TreeNode) t.children[2];

                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    }

                    else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        t.children[2] = null;
                    }
                }
                updateTree(t);
            }
        }
        //МЫ НАКОНЕЦ-ТО ОКАЗАЛИСЬ В УЗЛЕ ЛИСТОВОМ  ОТКУДА И ДОЛЖНО ПРОИСХОДИТЬ УДАЛЕНИЕ
        //поросту ищем место откуда удалить
        else if (t != null && t.children[0] instanceof LeafNode) {
            LeafNode l1 = null, l2 = null, l3 = null;
            if (t.children[0] != null && t.children[0] instanceof LeafNode) {
                l1 = (LeafNode) t.children[0];
            }
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            if (t.degree == 3) {

                if (key == l1.key) {
                    t.children[0] = l2;
                    t.children[1] = l3;
                    t.children[2] = null;
                } else if (key == l2.key) {
                    t.children[1] = l3;
                    t.children[2] = null;
                } else if (key == l3.key) {
                    t.children[2] = null;
                }

                updateTree(t);
            }
            else if (t.degree == 2) {
                underflow = true;
                if (l1.key == key) {
                    t.children[0] = l2;
                    t.children[1] = null;
                } else if (l2.key == key) {
                    t.children[1] = null;
                }
            }
            else if (t.degree == 1) {

                if (l1.key == key) {
                    t.children[0] = null;
                }
            }
            successfulDeletion = true;
        }
        return t;
    }
    private void updateTree(TreeNode t) {
        if (t != null) {
            if (t.children[2] != null && t.children[1] != null && t.children[0] != null) {
                t.degree = 3;
                t.keys[0] = getValueForKey(t, Nodes.LEFT);
                t.keys[1] = getValueForKey(t, Nodes.RIGHT);
            } else if (t.children[1] != null && t.children[0] != null) {
                t.degree = 2;
                t.keys[0] = getValueForKey(t, Nodes.LEFT);
                t.keys[1] = 0;
            } else if (t.children[0] != null) {
                t.degree = 1;
                t.keys[1] = t.keys[0] = 0;
            }
        }
    }
    private int getValueForKey(Node n, Nodes whichVal) {
        int key = -1;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (l != null) {
            key = l.key;
        }
        if (t != null) {
            if (null != whichVal) {
                switch (whichVal) {
                    case LEFT:
                        key = getValueForKey(t.children[1], Nodes.DUMMY);
                        break;
                    case RIGHT:
                        key = getValueForKey(t.children[2], Nodes.DUMMY);
                        break;
                    case DUMMY:
                        key = getValueForKey(t.children[0], Nodes.DUMMY);
                        break;
                    default:
                        break;
                }
            }
        }
        return key;
    }
    private boolean search(int key, Node n) {
        counter ++;
        boolean found = false;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {

            if (t.degree == 1) {

                found = search(key, t.children[0]);
            }
            else if (t.degree == 2 && key < t.keys[0]) {

                found = search(key, t.children[0]);
            }
            else if (t.degree == 2 && key >= t.keys[0]) {

                found = search(key, t.children[1]);
            }
            else if (t.degree == 3 && key < t.keys[0]) {

                found = search(key, t.children[0]);
            }
            else if (t.degree == 3 && key >= t.keys[0] && key < t.keys[1]) {

                found = search(key, t.children[1]);
            }
            else if (t.degree == 3 && key >= t.keys[1]) {

                found = search(key, t.children[2]);
            }
        }
        else if (l != null && key == l.key) {
            return true;
        }
        return found;
    }
    private void keyOrderList(Node n) {
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            if (t.children[0] != null) {
                keyOrderList(t.children[0]);
            }
            if (t.children[1] != null) {
                keyOrderList(t.children[1]);
            }
            if (t.children[2] != null) {
                keyOrderList(t.children[2]);
            }
        }
        else if (l != null) {
            System.out.print(l.key + " ");
        }
    }
//    private void bfsList(Node n) {
//        Queue<Node> queueOne = new LinkedList<>();
//        Queue<Node> queueTwo = new LinkedList<>();
//        if (n == null) {
//            return;
//        }
//        queueOne.add(n);
//        Node first = null;
//        TreeNode t = null;
//        while (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
//            while (!queueOne.isEmpty()) {
//                first = queueOne.poll();
//                if (first instanceof TreeNode) {
//                    t = (TreeNode) first;
//                    t.print();
//                }
//                if (t.children[0] != null && !(t.children[0] instanceof LeafNode)) {
//                    queueTwo.add(t.children[0]);
//                }
//                if (t.children[1] != null && !(t.children[1] instanceof LeafNode)) {
//                    queueTwo.add(t.children[1]);
//                }
//                if (t.children[2] != null && !(t.children[2] instanceof LeafNode)) {
//                    queueTwo.add(t.children[2]);
//                }
//
//            }
//            if (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
//                System.out.println();
//            }
//            while (!queueTwo.isEmpty()) {
//                first = queueTwo.poll();
//
//                if (first instanceof TreeNode) {
//                    t = (TreeNode) first;
//                    t.print();
//                }
//                if (t.children[0] != null && !(t.children[0] instanceof LeafNode)) {
//                    queueOne.add(t.children[0]);
//                }
//                if (t.children[1] != null && !(t.children[1] instanceof LeafNode)) {
//                    queueOne.add(t.children[1]);
//                }
//                if (t.children[2] != null && !(t.children[2] instanceof LeafNode)) {
//                    queueOne.add(t.children[2]);
//                }
//            }
//            if (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
//                System.out.println();
//            }
//        }
//        System.out.println();
//        keyOrderList(root);
//        System.out.println();
//    }
    public boolean insert(int key) {
        counter = 0;
        boolean insert = false;
        split = false;
        double start = System.nanoTime();
        if (!search(key)) {
            counter = 0;
            insertKey(key);
        }
        if (successfulInsertion) {
            size++;
            insert = successfulInsertion;
            successfulInsertion = false;
        }
        double end = System.nanoTime();
        time = end - start;
        return insert;
    }
    public boolean search(int key) {
        counter = 0;
        double start = System.nanoTime();
        boolean z =  search(key, root);
        double end = System.nanoTime();
        double time = end - start;
        return z;
    }
    public boolean remove(int key) {
        counter = 0;
        boolean delete = false;
        singleNodeUnderflow = false;
        underflow = false;
        double start = System.nanoTime();
        if (search(key)) {
            counter = 0;
            root = (TreeNode) remove(key, root);
            if (root.degree == 1 && root.children[0] instanceof TreeNode) {

                root = (TreeNode) root.children[0];
            }
        }
        if (successfulDeletion) {
            size--;
            delete = successfulDeletion;
            successfulDeletion = false;
        }
        double end = System.nanoTime();
        time = end -start;
        return delete;
    }
//    public void bfsList() {
//        System.out.println("Tree");
//        bfsList(root);
//    }

    public double getTime() {
        return time;
    }
    public int getCounter() {
        return counter;
    }
}