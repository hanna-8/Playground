#include "Sogo.h"

#include <list>
#include <queue>


// Returns true if solvable
bool Sogo::solve(const SogoBoard& board) const {

	if (board.solved())
		return true;

	auto boxCell = *(board.find(Dict::box));	// Not solved => not null.
	auto boxPath = shortestPath(board, boxCell, *(board.find(Dict::dest)), [](SogoBoard::Cell from, SogoBoard::Cell to) {
		return board.isFree(to) == true && (board.isFree(board.mirrorCell(from, to) || board.at(board.mirrorCell(from, to) == Dict::robot));
	});	// Optional

	if (!boxPath)
		return false;

	for (nextCell: *boxPath) {
		pushBox(board, boxCell, nextCell);
		boxCell = nextCell;
	}

	return run(board);
}


// Push box one cell
void Sogo::pushBox(SogoBoard board, SogoBoard::Cell boxCell, SogoBoard::Cell nextCell) {
	auto robotCell = board.find(Dict::robot);
	auto robotDest = board.mirrorCell(boxCell, nextCell);
	auto robotPath = shortestPath(board, robotCell, robotDest, board.isFree);	// Optional

	for (nextCell: robotPah) {
		board.move(robotCell, nextCell);
		robotCell = nextCell;
	}

	board.move(robotCell, boxCell);
}


boost::optional<std::stack<SogoBoard::Cell>> Sogo::shortestPath(SogoBoard& board, SogoBoard::Cell source, SogoBoard::Cell dest, std::function<bool(SogoBoard::Cell, SogoBoard::Cell)> moveIsValid) {

	std::queue<SogoBoard::Cell> remaining;
	std::map<SogoBoard::Cell, SogoBoard::Cell> predecessors;
	boost::optional<std::stack<SogoBoard::Cell>> pathOrNot;

	predecessors[source] = source;

	std::function<bool(SogoBoard::Cell)> shortestRec sh = [&](SogoBoard::Cell current) {
		if (current == dest)
			return true;

		std::vector<Cell> neighbors(4);
		auto neighborMaths = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
		for (auto [first, second]: neighborMaths)
			neighbors.add(Cell(c.x() + first, c.y() + second));

		std::remove_if(neighbors.begin(), neighbors.end(), [](SogoBoard::Cell n) { 
			!moveIsValid(current, n) || predecessors.containsKey(n);

		for(n: neighbors) {
			predecessors[n] = current;
			remaining.push(n);
		}

		if (remaining.empty())
			return false;

		return sh(remaining.pop());
	}

	if (!sh(source)) {
		Cell current = dest;
		auto path;
		while (current != source) {
			path.push(current);
			current = predecessors[current];
		}
		pathOrNot = path;
	}

	return pathOrNot;
}
