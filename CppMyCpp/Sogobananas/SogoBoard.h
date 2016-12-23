#include <boost/optional/optional.hpp>

#include <iostream>
#include <fstream>
#include <vector>


enum Dict {
	free = 0,
	obstacle = 1,
	box = 2,
	dest = 3,
	robot = 4,
	full = 5	// dest + box
}

//template <type T>
class SogoBoard {
public: 
	SogoBoard(): m_height(0), m_width(0) {};
	~SogoBoard() {};

	struct Cell {
		Cell(std::size_t x, std::size_t y): p(std::make_pair(x, y)) {}
		std::size_t x() { return p.first; } 
		std::size_t y() { return p.second; } 

	private: 	
		std::pair<std::size_t, std::size_t> p;
	};

	bool solved() const;
	boost::optional<Cell> find(Dict type) const;
	bool isFree(Cell c) const;
	board.mirrorCell(from, to);
	Cell board.mirrorCell(Cell origin, Cell reflected) const;
	void swapContent(Cell c1, Cell c2);
	std::vector<Cell> getFreeNeighborCells() const;

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