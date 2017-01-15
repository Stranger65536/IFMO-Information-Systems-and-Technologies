#include <iostream>
#include <conio.h>
#include <algorithm>

using namespace std;

const int c = 6;

float correlation(int u, int v, int**r)
{
	float pc1, pc2, pc3, rv, ru;
	auto cv = 0, cu = 0;

	ru = rv = pc1 = pc2 = pc3 = 0.0;

	for (auto i = 0; i < c; i++)
	{
		if (r[u-1][i] != -1) 
		{
			ru += r[u-1][i];
			cu++;
		}
		if (r[v-1][i] != -1)
		{
			rv += r[v-1][i];
			cv++;
		}
	}

	ru /= cu;
	rv /= cv;

	for (auto i = 0; i < c; i++)
	{
		if (r[u-1][i] != -1 && r[v-1][i] != -1)
		{
			pc1 += (r[u-1][i]-ru) * (r[v-1][i]-rv);
			pc2 += (r[u-1][i]-ru) * (r[u-1][i]-ru);
			pc3 += (r[v-1][i]-rv) * (r[v-1][i]-rv);
		}
	}

	return pc1 / sqrt(pc2 * pc3);
}

bool sort_function (std::pair<int, float> l, std::pair<int, float> r) 
{ 
	return l.second > r.second; 
}

int main()
{
	auto raiting = new int*[6];
	int zero[c]   = { 2, 4,-1, 5,-1,-1 };
	int first[c]  = { 3, 4,-1,-1, 5, 1 };
	int second[c] = {-1,-1, 5, 5, 4,-1 };
	int third[c]  = {-1, 4,-1, 4, 5, 4 };
	int fourth[c] = { 2,-1, 5, 5, 2,-1 };
	int fifth[c]  = { 2, 4,-1, 5, 4,-1 };

	raiting[0] = zero;
	raiting[1] = first;
	raiting[2] = second;
	raiting[3] = third;
	raiting[4] = fourth;
	raiting[5] = fifth;

	auto user = 1;
	float pc[c - 1];

	cout << "Correlation with user 1: "<< endl;

	for (auto i = 0; i < c; i++)
	{
		if (i != user - 1) 
		{
			pc[i - 1] = correlation (user, i + 1, raiting); // pc- массив коэф кор.
			cout << "user " << i + 1 << " " << pc[i - 1] << endl;
		}
	}

	cout << endl;

	std::pair<int, double> users[c - 1];

	for (auto j = 0; j < c - 1; j++)
	{
		users[j] = std::make_pair(j, pc[j]);
		users[j] = users[j];
	}

	std::sort (users, users + (c - 1), sort_function);

	for (auto i = 0; i < c; i++)
	{
		if (raiting[user - 1][i] == -1)
		{
			auto found = false;
			for (auto j = 0; j < c - 1; j++)
			{
				auto current = users[j];
				if (raiting[current.first][i] != -1 && !found)
				{
					if (raiting[current.first][i] > 3) 
					{
						cout << "good item found: " << i + 1<< endl;
					}
					else 
					{
						cout << "item is not good: " << i + 1 << endl;
					}
					found = true;
				}
			}

			if (!found) 
			{
				cout << "I dont't know anything about this item: " << i + 1 << endl;
			}
		}
	}

	_getch();
}