package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
    public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

        ArrayList<String> shortest = new ArrayList<>();

        
        if(g == null || p1 == null || p2 == null || p1.length() == 0 || p2.length() == 0) {
            return null;
        }
       
        p1 = p1.toLowerCase();
        p2 = p2.toLowerCase();

        if(p1.equals(p2)) {
            shortest.add(g.members[g.map.get(p1)].name);
        
            return shortest;
        }

        
        if(g.map.get(p1) == null || g.map.get(p2) == null) {
            System.out.println("p1 or p2 does not exist");
            
            return null;
        }

        
        Queue<Integer> queue = new Queue<>();
        int[] dist = new int[g.members.length];
        int[] pred = new int[g.members.length];
        boolean[] visited = new boolean[g.members.length]; 

        
        for(int i = 0; i < visited.length; i++) {
            visited[i] = false;
            pred[i] = -1;
        }

        int startIndex = g.map.get(p1);
        Person startPerson = g.members[startIndex];
        
        visited[startIndex] = true;
        dist[startIndex] = 0;
        
        queue.enqueue(startIndex);

            while(!queue.isEmpty()) {
            
                int vi = queue.dequeue(); 
                Person pr = g.members[vi];

            
            for(Friend ptr = pr.first; ptr != null; ptr=ptr.next) {
                int fnum = ptr.fnum;

                
                if(!visited[fnum]){
                    dist[fnum] = dist[vi]+1; 
                    pred[fnum] = vi;
                    visited[fnum] = true;
                    queue.enqueue(fnum);
                }
            }
        }

       
        Stack<String> path = new Stack<>();
        int spot = g.map.get(p2);

        if(!visited[spot]) {
            System.out.println("Cannot reach");
            
            return null;
        }

        while(spot != -1) {
            path.push(g.members[spot].name);
            spot = pred[spot];
        }

        while(!path.isEmpty()) {
            shortest.add(path.pop());
        }

        return shortest;
    }



    public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

        ArrayList<ArrayList<String>> answer = new ArrayList<>();

        
        if(g == null || school == null || school.length() == 0) {
            return null;
        }

      
        school = school.toLowerCase();

        boolean[] visited = new boolean[g.members.length];
            for(int i = 0; i < visited.length; i++) {
                visited[i] = false;
        }

        for(Person member : g.members) {

            if(!visited[g.map.get(member.name)] && member.school != null && member.school.equals(school)) {

                Queue<Integer> queue = new Queue<>();
                ArrayList<String> clique = new ArrayList<>();

                int startIndex = g.map.get(member.name);
                visited[startIndex] = true;

                queue.enqueue(startIndex);
                clique.add(member.name);

                while(!queue.isEmpty()) {
                    int vi = queue.dequeue(); 
                    Person pr = g.members[vi];


                    for(Friend ptr = pr.first; ptr != null; ptr=ptr.next) {
                        int fnum = ptr.fnum;
                        Person fr = g.members[fnum];


                        if(!visited[fnum] && fr.school != null && fr.school.equals(school)){
                            visited[fnum] = true;
                            queue.enqueue(fnum);
                            clique.add(g.members[fnum].name);
                        }
                    }
                }

                answer.add(clique);
            }
        }

        return answer;
    }

    public static ArrayList<String> connectors(Graph g) {

        boolean[] visited = new boolean[g.members.length]; 
        int[] dfsnum = new int[g.members.length];
        int[] back = new int[g.members.length];
        ArrayList<String> answer = new ArrayList<>();

        for(Person member : g.members) {

            if(!visited[g.map.get(member.name)]){
                dfsnum = new int[g.members.length];
                dfs(g.map.get(member.name), g.map.get(member.name), g, visited, dfsnum, back, answer);
            }
        }

        
        for(int i = 0; i < answer.size(); i++) {
            Friend ptr = g.members[g.map.get(answer.get(i))].first;

            int count = 0;
            while(ptr != null) {
                ptr = ptr.next;
                count++;
            }

            if(count == 0 || count == 1) {
                answer.remove(i);
            }
        }


        for(Person member : g.members) {
            if((member.first.next == null && !answer.contains(g.members[member.first.fnum].name))) {
                answer.add(g.members[member.first.fnum].name);
            }
        }

        return answer;
    }


    private static int sizeArr(int[] arr) {
        int count = 0;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] != 0) {
                count++;
            }
        }
        return count;
    }

    private static void dfs(int vi, int start, Graph g, boolean[] visited, int[] dfsnum, int[] back, ArrayList<String> answer){

        Person pr = g.members[vi];
        visited[g.map.get(pr.name)] = true;
        int count = sizeArr(dfsnum)+1;

      
        if(dfsnum[vi] == 0 && back[vi] == 0) {
            dfsnum[vi] = count;
            back[vi] = dfsnum[vi];
        }

    
        for(Friend ptr = pr.first; ptr != null; ptr = ptr.next) {
    
            if(!visited[ptr.fnum]) {


                dfs(ptr.fnum, start, g, visited, dfsnum, back, answer);


                if(dfsnum[vi] > back[ptr.fnum]) {
                    back[vi] = Math.min(back[vi], back[ptr.fnum]);
                
                } else {
                    if(Math.abs(dfsnum[vi]-back[ptr.fnum]) < 1 && Math.abs(dfsnum[vi]-dfsnum[ptr.fnum]) <=1 && back[ptr.fnum] ==1 && vi == start) {
                        continue;
                    }


                    if(dfsnum[vi] <= back[ptr.fnum] && (vi != start || back[ptr.fnum] == 1 )) { 
                        if(!answer.contains(g.members[vi].name)) {
                            answer.add(g.members[vi].name);
                        }
                    }

                }
            } else {
                back[vi] = Math.min(back[vi], dfsnum[ptr.fnum]);
            }
        }
        return;
    }

}