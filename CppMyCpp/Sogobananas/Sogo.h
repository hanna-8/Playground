#include "SogoBoard.h"
#include <vector>

class Sogo {
public:
	Sogo() {};
	~Sogo() {};

	void run(const SogoBoard& board) const;

private:

	std::vector<int> shortestPath(const SogoBoard& board, const SogoBoard::Coords here, SogoBoard::Coords dest);
};