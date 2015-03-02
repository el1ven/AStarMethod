package jp.co.worksap.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AStar {

    public static class Point{

        public Point parentPoint;//父节点,用于最后回溯路径
        public int F;//F=G+H
        public int G;
        public int H;
        public int x;//点的坐标
        public int y;

        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        public void calulateF(){
            this.F = this.G + this.H;
        }

        public Point getParentPoint() {
            return parentPoint;
        }

        public void setParentPoint(Point parentPoint) {
            this.parentPoint = parentPoint;
        }

        public int getF() {
            return F;
        }

        public void setF(int f) {
            F = f;
        }

        public int getG() {
            return G;
        }

        public void setG(int g) {
            G = g;
        }

        public int getH() {
            return H;
        }

        public void setH(int h) {
            H = h;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

    };

    //对List<Point>的一些扩展方法
    public static class ListHelper{

        public static boolean Exists(ArrayList<Point> points, Point point){
            for(Point p : points){//检查点
                if(p.x == point.x && p.y == point.y){
                    return true;
                }
            }
            return false;
        }

        public static boolean Exists(ArrayList<Point> points, int x, int y){
            for(Point p : points){//检查坐标
                if(p.x == x && p.y == y){
                    return true;
                }
            }
            return false;
        }

        public static Point minPoint(ArrayList<Point> points){
            Collections.sort(points, new Comparator<Point>() {
                @Override
                public int compare(Point p1, Point p2) {
                    if (p1.F < p2.F) {
                        return 1;//选出F值最小的点
                    } else if (p1.F > p2.F) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            return points.get(0);
        }

        public static void addPoint(ArrayList<Point> points, int x, int y){
            Point point = new Point(x,y);
            points.add(point);
        }

        public static Point getPoint(ArrayList<Point> points, Point point){
            for(Point p : points){
                if(p.x == point.x && p.y == point.y){
                    return p;
                }
            }
            return null;
        }

        public static void removePoint(ArrayList<Point> points, int x, int y){
            for(Point p : points){
                if(p.x == x && p.y == y){
                    points.remove(p);
                }
            }
        }

    }

    public final int obliqueValue = 14;//倾斜代价
    public final int normalValue = 10;//上下左右平移代价

    public int[][] map;//游戏地图

    public ArrayList<Point> openList;//开启列表
    public ArrayList<Point> closeList;//关闭列表

    public AStar(int [][] map){
        this.map = map;
        this.openList = new ArrayList<Point>(map.length);
        this.closeList = new ArrayList<Point>(map.length);
    }

    public Point findPath(Point start, Point end, boolean isIgnoreCorner){
        openList.add(start);
        while(openList.size() != 0){
            //找出F值最小的点
            Point tempStart = ListHelper.minPoint(openList);
            openList.remove(0);
            closeList.add(tempStart);
            //找出它相邻的点
            ArrayList<Point> surroundPoints = getSurroundPoints(tempStart, isIgnoreCorner);
            for(Point p : surroundPoints){
                if(ListHelper.Exists(openList, p)){
                    //计算G的值，如果比原来的大，就什么都不做，否则将它设置为父节点，相应更新G和F
                    foundPoint(tempStart, p);
                }else{
                    //如果它们不在开始列表里，就加入，并设置父节点，并计算GHF
                    notFoundPoint(tempStart, end, p);
                }
            }
            if(ListHelper.getPoint(openList, end) != null){
                return ListHelper.getPoint(openList, end);
            }
        }
        return ListHelper.getPoint(openList, end);
    }

    private void foundPoint(Point tempStart, Point point){
        int G = calculateG(tempStart, point);
        if(G < point.G){
            point.parentPoint = tempStart;
            point.G = G;
            point.calulateF();
        }
    }

    private void notFoundPoint(Point tempStart, Point end, Point point){
        point.parentPoint = tempStart;
        point.G = calculateG(tempStart, point);
        point.H = calculateH(end, point);
        point.calulateF();
        openList.add(point);
    }

    private int calculateG(Point start, Point point){
        int G = (Math.abs(point.x - start.x) + Math.abs(point.y - start.y) == 2 ? normalValue : obliqueValue);
        int parentG = point.parentPoint != null ? point.parentPoint.G : 0;
        return G + parentG;
    }

    private int calculateH(Point end, Point point){
        int step = Math.abs(point.x - end.x) + Math.abs(point.y - end.y);
        return normalValue*step;
    }

    //获取周围可以到达的点
    public ArrayList<Point> getSurroundPoints(Point point, boolean isIgnoreCorner){
        ArrayList<Point> surroundPoints = new ArrayList<Point>(9);
        for(int x = point.x-1; x <= point.x+1; x++){
            for(int y = point.y-1; y <= point.y+1; y++){
                if(canReach(point, x, y, isIgnoreCorner)){
                    ListHelper.addPoint(surroundPoints, x, y);
                }
            }
        }
        return surroundPoints;
    }

    //在二维数组对应的位置不为障碍物
    private boolean canReach(int x, int y){
        return map[x][y] == 0;//0表示无障碍
    }

    public boolean canReach(Point start, int x, int y, boolean isIgnoreCorner){
        if(!canReach(x,y) || ListHelper.Exists(closeList, x, y)){
            return false;
        }else{
            if(Math.abs(x-start.x) + Math.abs(y-start.y) == 1){
                //正常走
                return true;
            }else{
                //对角走
                if(canReach(Math.abs(x-1), y) && canReach(x, Math.abs(y-1))){
                    return true;
                }else{
                    return isIgnoreCorner;
                }
            }
        }
    }
}
