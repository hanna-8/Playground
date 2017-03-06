#pragma once

#include <iostream>
#include <fstream>
#include <vector>


class SogoBoard {
public: 
  SogoBoard();
//  SogoBoard(std::size_t width, std::size_t height, T value);
  ~SogoBoard() {};

  struct Coords {
    std::size_t x;
    std::size_t y;
  }
  
  struct Cell {

    Cell(Coords newCoords): coords(newCoords), val(free) {}
    
//    Loc& operator+(const Loc& rhs) { return Loc(x + rhs.x, y + rhs.y); }
    
    Coords coords;
    Value val;
  };

  enum Value {
    free = 0,
    obstacle = 1,
    box = 2,
    dest = 3,
    robot = 4,
    full = 5	// dest + box
  }

//  bool solved() const;
//  boost::optional<Cell> find(Dict type) const;
//	bool isFree(Cell c) const;
//	boost::optional<Cell> mirrorCell(Cell from, Cell to) const;
//	std::vector<Cell> getFreeNeighborCells() const;
	
//	bool move(Cell robotCell, Cell nextCell);

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


inline
std::ostream& operator<< (std::ostream& out, SogoBoard const& sb) {
	for(std::size_t i = 0; i < sb.m_height; ++i) {
		for(std::size_t j = 0; j < sb.m_width; ++j)
			out << sb.m_board.at(i * sb.m_width + j) << ' ';
		out << std::endl;
	}
	return out;
}


inline
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
