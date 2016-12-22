#include "SogoBoard.h"

bool SogoBoard::solved() { 
	for (c: m_board) 
		if (c == Dict::box) 
			return false;
	return true;
}

boost::optional<SogoBoard::Cell> SogoBoard::find(Dict type) {
	boost::optional<Cell> found;
	for (c: m_board)
		if (c == type) {
			found = c;
			break;
		}

	return found;
}

bool SogoBoard::isFree(Cell c) const {
	return m_board.at(c.x() * m_width + c.y()) == Dict.free;
}

boost::optional<SogoBoard::Cell> SogoBoard::mirrorCell(SogoBoard::Cell origin, ) {