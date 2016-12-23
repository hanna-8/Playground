#include "SogoBoard.h"


boost::optional<SogoBoard::Cell> SogoBoard::find(Dict type) {
	boost::optional<Cell> found;
	for (c: m_board)
		if (c == type) {
			found = c;
			return found;
		}

	return found;
}

bool SogoBoard::isFree(Cell c) const {
	return m_board.at(c.x() * m_width + c.y()) == Dict::free;
}

boost::optional<SogoBoard::Cell> SogoBoard::mirrorCell(SogoBoard::Cell origin, SogoBoard::Cell toReflect) {
	boost::optional<Cell> reflected;

	if (origin.x() == toReflect.x())
		reflected = Cell(origin.x(), origin.x() * 2 - toReflect.x());
	else if (origin.y() == toReflect.y())
		reflected = Cell(origin.x() * 2 - toReflect.x(), origin.y());

	return reflected;
}

bool SogoBoard::solved() { 
	for (c: m_board) 
		if (c == Dict::box) 
			return false;
	return true;
}

/**
 * R -> _			=> 		_ -> R
 * R -> B -> _		=>		_ -> R -> B
 * R -> B -> D 		=> 		_ -> R -> F
 */
bool Sogo::moveRobot(Cell robotCell, Cell nextCell) {
	if (at(nextCell) == Dict::box) {
		auto nextBoxCell = mirrorCell(nextCell, robotCell);
		if (at(*nextBoxCell) == Dict::free)	// R -> B -> _
			at(*nextBoxCell) = Dict::box;
		else if (at(*nextBoxCell) == Dict.dest)	// R -> B -> D
			at(*nextBoxCell) = Dict.full;
		else 
			return false;
	}
	else if (at(nextCell) == Dict::free) {
		at(robotCell) = Dict.free;
		at(nextCell) = Dict.robot;
		return true;
	}

	return false;
}