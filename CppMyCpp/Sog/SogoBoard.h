#include <boost/optional/optional.hpp>

#include <iostream>
#include <fstream>
#include <vector>


template <type T>
class SogoBoard {
public: 
	SogoBoard();
	SogoBoard(std::size_t width, std::size_t height, T value);
	~SogoBoard() {};

	struct Loc {
		Loc(std::size_t newx, std::size_t newy): x(newx), y(newy) {}
		Loc& operator+(const Loc& rhs) { return Loc(p.x + rhs.x, p.y + rhs.y; }
	private:
		size_t x;
		size_t y;
	};

	bool solved() const;
	boost::optional<Cell> find(Dict type) const;
	bool isFree(Cell c) const;
	boost::optional<Cell> mirrorCell(Cell from, Cell to) const;
	std::vector<Cell> getFreeNeighborCells() const;
	
	bool move(Cell robotCell, Cell nextCell);

	// int at(std::pair<std::size_t, std::size_t> location) const { return m_board.at(location.first * m_width + location.second); };	// throws index_out_of_bounds
	// void set(std::pair<std::size_t, std::size_t> location, int i) { m_board.at(location.first * m_width + location.second) = i; };	// throws index_out_of_bounds

	// bool isVisitable(const std::pair<std::size_t, std::size_t> p) { return p.first < m_height && p.second < m_width && at(p) == 0; }

	friend std::istream& operator>> (std::istream& in, SogoBoard& sb);
	friend std::ostream& operator<< (std::ostream& out, SogoBoard const& sb);

private:
	std::size_t m_height;
	std::size_t m_width;
	std::vector<int> m_board;
};


std::ostream& operator<< (std::ostream& out, SogoBoard const& sb) {
	for(std::size_t i = 0; i < sb.m_height; ++i) {
		for(std::size_t j = 0; j < sb.m_width; ++j)
			out << sb.m_board.at(i * sb.m_width + j) << ' ';
		out << std::endl;
	}
	return out;
}


std::istream& operator>> (std::istream& in, SogoBoard& sb) {
	in >> sb.m_height >> sb.m_width;
	sb.m_board.assign(sb.m_height * sb.m_width, 0);

	while (!in.eof())
	{
		int type = 0;
		std::size_t row = 0, col = 0;

		in >> type >> row >> col;
		sb.m_board.at(row * sb.m_height + col) = type;
	}

	return in;
}