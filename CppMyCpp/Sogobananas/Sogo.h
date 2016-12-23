#include "SogoBoard.h"
#include <vector>


class Sogo {
public:
	Sogo() {};
	~Sogo() {};

	void run(const SogoBoard& board) const;

private:
	enum Dict {
		free = 0,
		obstacle = 1,
		box = 2,
		dest = 3,
		robot = 4,
		full = 5	// dest + box
	}

	boost::optional<std::stack<SogoBoard::Cell>> shortestPath(const SogoBoard& board, const SogoBoard::Coords source, SogoBoard::Coords dest);
};