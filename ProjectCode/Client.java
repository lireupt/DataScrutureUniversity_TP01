/**
   * All material is for learning only.
   * All who use this material - whatever the purpose:
   * - must keep the reference to the original material created by:
   * - Helder Oliveira / Denis Botnaru
   * and available at the address of your repository:
   * - https://github.com/lireupt/ public.
   * This material may not be used in no circumstance to replicate:
   * - integral or partially, with purposes of obtaining financial gains from it.
   */


import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.*;


class Client {
	public static void main(String[] args) throws IOException {
		Data pb = new Data();
		pb.readPbm("C:input.pbm");
		pb.detectJoints();
		pb.detectEndPoints();
		pb.compareLines();
	}
}