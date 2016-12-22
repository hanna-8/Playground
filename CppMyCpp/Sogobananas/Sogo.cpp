#include "Sogo.h"

#include <list>
#include <queue>


// Returns true if solvable
bool Sogo::run(const SogoBoard& board) const {

	if (board.solved())
		return true;

	auto boxCell = board.find(Dict.box);
	auto boxPath = shortestPath(board, boxCell, board.find(Dict.dest), [](SogoBoard::Cell from, SogoBoard::Cell to) {
		return board.isFree(to) == true && board.isFree(board.mirrorCell(from, to));
	});	// Optional

	if (!boxPath)
		return false;

	for (nextCell: *boxPath) {
		pushBox(board, boxCell, nextCell);
		boxCell = nextCell;
	}

	return run(board);
}


void Sogo::pushBox(SogoBoard board, SogoBoard::Cell boxCell, SogoBoard::Cell nextCell) {
	auto robotCell = board.findRobot();
	auto robotDest = board.mirrorCell(boxCell, nextCell);
	auto robotPath = shortestPath(board, robotCell, robotDest, board.isFree);	// Optional

	for (nextCell: robotPah) {
		board.swap(robotCell, nextCell);
		robotCell = nextCell;
	}

	board.pushBox(robotCell, boxCell);
}


bool Sogo::shortestPath(SogoBoard& board, SogoBoard::Cell source, SogoBoard::Cell dest, std::function<bool(SogoBoard::Cell, SogoBoard::Cell)> moveIsValid) {

	std::queue<SogoBoard::Cell> remaining;
	std::map<SogoBoard::Cell, SogoBoard::Cell> predecessors;

	predecessors[source] = source;

	std::function<bool(SogoBoard::Cell)> shortestRec sh = [&](SogoBoard::Cell current) {
		if (current == dest)
			return true;

		std::vector<SogoBoard::Cell> neighbors = {
			{ current.first - 1, current.second }, 
			{ current.first + 1, current.second },
			{ current.first, current.second - 1 },
			{ current.first, current.second + 1 },
		};
		std::remove_if(neighbors.begin(), neighbors.end(), [](SogoBoard::Cell n) { 
			!moveIsValid(current, n) || predecessors.containsKey(n);
		});

		for(n: neighbors) {
			predecessors[n] = current;
			remaining.push(n);
		}

		if (remaining.empty())
			return false;

		return sh(remaining.pop());
	}

	return sh(source);
}