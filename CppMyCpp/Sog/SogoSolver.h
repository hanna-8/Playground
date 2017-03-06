#include <boost/optional.hpp>

#include "SogoBoard.h"


class SogoSolver {
public:
	SogoSolver() {};
	~SogoSolver() {};

	void solve(const SogoBoard& board) const;

private:

	boost::optional<std::stack<SogoBoard::Cell>> shortestPath(const SogoBoard& board, const SogoBoard::Coords source, SogoBoard::Coords dest);
};
