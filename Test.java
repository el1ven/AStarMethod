package jp.co.worksap.global;

import java.util.Stack;

/**
 * Created by el1ven on 28/2/15.
 */
public class Test {
    public static void main(String[] args){

        int[][] map = new int[][]{
                {1,1,1,1,1,1,1,1},
                {1,0,0,0,1,0,0,1},
                {1,0,0,0,1,0,0,1},
                {1,0,0,0,1,0,0,1},
                {1,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1},
        };

        AStar aStar = new AStar(map);

        AStar.Point start = new AStar.Point(1, 1);
        AStar.Point end = new AStar.Point(4, 3);
        AStar.Point parent = aStar.findPath(start, end, false);

        System.out.println("Print Path: ");
        Stack<AStar.Point> AStarRoute = new Stack<AStar.Point>();

        while(parent != null){
            AStarRoute.push(parent);
            parent = parent.getParentPoint();
        }

        while(!AStarRoute.empty()){//正序输出
            AStar.Point p = AStarRoute.peek();//获取栈顶元素
            System.out.println(p.getX()+","+p.getY());
            AStarRoute.pop();
        }

    }
}
