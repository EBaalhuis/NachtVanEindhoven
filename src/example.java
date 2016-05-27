import java.util.Collection;

import jsprit.analysis.toolbox.GraphStreamViewer;
import jsprit.analysis.toolbox.Plotter;
import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.box.Jsprit;
import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.io.VrpXMLWriter;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleType;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.reporting.SolutionPrinter;
import jsprit.core.reporting.SolutionPrinter.Print;
import jsprit.core.util.Solutions;

public class example {
	public static void main(String[] args) {
		
		/*
		 * get a vehicle type-builder and build a type with the typeId "vehicleType" and a capacity of 2
		 * you are free to add an arbitrary number of capacity dimensions with .addCacpacityDimension(dimensionIndex,dimensionValue)
		 */
		final int WEIGHT_INDEX = 0;
		VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("vehicleType").addCapacityDimension(WEIGHT_INDEX,2);
		VehicleType vehicleType = vehicleTypeBuilder.build();

		/*
		 * get a vehicle-builder and build a vehicle located at (10,10) with type "vehicleType"
		 */
		VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle");
		vehicleBuilder.setStartLocation(Location.newInstance(10, 10));
		vehicleBuilder.setType(vehicleType); 
		VehicleImpl vehicle = vehicleBuilder.build();
		
		/*
		 * build services with id 1...4 at the required locations, each with a capacity-demand of 1.
		 * Note, that the builder allows chaining which makes building quite handy
		 */
		Service service1 = Service.Builder.newInstance("1").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(5, 7)).build();
		Service service2 = Service.Builder.newInstance("2").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(5, 13)).build();
		Service service3 = Service.Builder.newInstance("3").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(15, 7)).build();
		Service service4 = Service.Builder.newInstance("4").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(15, 13)).build();
		
		/*
		 * again define a builder to build the VehicleRoutingProblem
		 */
		VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder.addVehicle(vehicle);
		vrpBuilder.addJob(service1).addJob(service2).addJob(service3).addJob(service4);
		/*
		 * build the problem
		 * by default, the problem is specified such that FleetSize is INFINITE, i.e. an infinite number of 
		 * the defined vehicles can be used to solve the problem
		 * by default, transport costs are computed as Euclidean distances
		 */
		VehicleRoutingProblem problem = vrpBuilder.build();
		
		/*
		* get the algorithm out-of-the-box. 
		*/
		VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);

		/*
		* and search a solution which returns a collection of solutions (here only one solution is constructed)
		*/
		Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

		/*
		 * use the static helper-method in the utility class Solutions to get the best solution (in terms of least costs)
		 */
		VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
		
		new VrpXMLWriter(problem, solutions).write("output/problem-with-solution.xml");

		
		// output
		//SolutionPrinter.print(problem, bestSolution, Print.CONCISE);
		//SolutionPrinter.print(problem, bestSolution, Print.VERBOSE);
		//new Plotter(problem,bestSolution).plot("output/solution.png", "solution");
		new GraphStreamViewer(problem, bestSolution).setRenderDelay(100).display();
		
	}
}
