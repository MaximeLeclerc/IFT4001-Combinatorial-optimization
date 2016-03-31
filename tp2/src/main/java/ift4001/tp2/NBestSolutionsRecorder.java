package ift4001.tp2;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.objective.ObjectiveManager;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.search.solution.ISolutionRecorder;
import org.chocosolver.solver.search.solution.Solution;
import org.chocosolver.solver.variables.IntVar;

import com.google.common.collect.MinMaxPriorityQueue;

public class NBestSolutionsRecorder implements ISolutionRecorder {

    private final Comparator<Integer> objectiveComparator;
    private final Comparator<Solution> solutionComparator;

    private final int n;
    private final IntVar objective;

    private final MinMaxPriorityQueue<Solution> bestSolutions;

    public NBestSolutionsRecorder(int n, IntVar objective) {
        this.n = n;
        this.objective = objective;

        this.objective.getSolver()
                .set(new ObjectiveManager<IntVar, Integer>(objective, ResolutionPolicy.MINIMIZE, false) {
                    @Override
                    public void update() {
                        // noop
                    }
                });

        if (this.objective.getSolver().getObjectiveManager().getPolicy() == ResolutionPolicy.MINIMIZE) {
            this.objectiveComparator = (o1, o2) -> o1.compareTo(o2);
        } else {
            this.objectiveComparator = (o1, o2) -> o2.compareTo(o1);
        }
        this.solutionComparator = (s1, s2) -> {
            return this.objectiveComparator.compare(s1.getIntVal(this.objective), s2.getIntVal(this.objective));
        };
        this.bestSolutions = MinMaxPriorityQueue.orderedBy(solutionComparator).maximumSize(n).create();

        this.objective.getSolver().plugMonitor(createRecMonitor());
    }

    @SuppressWarnings("unchecked")
    protected IMonitorSolution createRecMonitor() {
        return () -> {
            Solution solution = new Solution();
            solution.record(this.objective.getSolver());

            if (this.bestSolutions.offer(solution)) {
                if (this.bestSolutions.size() == n) {
                    if (this.objective.getSolver().getObjectiveManager().getPolicy() == ResolutionPolicy.MINIMIZE) {
                        this.objective.getSolver().getObjectiveManager()
                                .updateBestUB(this.bestSolutions.peekLast().getIntVal(this.objective));
                    } else {
                        this.objective.getSolver().getObjectiveManager()
                                .updateBestLB(this.bestSolutions.peekLast().getIntVal(this.objective));
                    }
                }
            }
        };
    }

    @Override
    public Solution getLastSolution() {
        return this.bestSolutions.peekLast();
    }

    @Override
    public List<Solution> getSolutions() {
        return Stream.generate(() -> this.bestSolutions.removeFirst()).limit(this.bestSolutions.size())
                .collect(Collectors.toList());
    }
}
