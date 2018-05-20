# Project's Remote Service Analysis!

The data persistence in this project is achieved through the FénixFramework, the FénixFramework implements **optimistic concurrency control**  (OCC).

**OCC** assumes that multiple transactions can frequently complete without interfering with each other. While running, transactions use data resources without acquiring locks on those resources. 

Before committing, each transaction verifies that no other transaction has modified the data it has read. If the check reveals conflicting modifications, the committing transaction rolls back and can be restarted.


# Testing

The tool we're using for automatic load tests is **Jmeter**.
The following tests are implemented:

**Load tests:**
 - **100 Writes** - initializes data and simulates 100 users trying to process their adventure
 - **100 Reads** - initializes data and simulates 500 users reading information 
 - **30 Writes** - initializes data and simulates 500 users processing and reading information.
 
 
 

## Analysis
Analysing results found at the end of file for each test:

**100 Writes** 
The test files have a high collision of client nifs, simulating a worst case scenario of forcing multiple collisions.
With an error rate of 14% on the worst case scenario on processing shows a small price to pay for the optimization that the optimistic approach gives.

**100 Reads** 
Tests for only readers.
With an error rate of 0% shows how effective and efficient the optimistic approach is when no collisions are found.

**30 Reads** 
The test files have a high collision of client nifs, simulating a worst case scenario of forcing multiple collisions. This test simulates a more realistic situation where there are users reading and writting at the same time.

The high amount of errors due to high collision rate shows the downside of the optimistic approach by FenixFramework in the cases where the collisions are very high. 


## Conclusion
Looking at the results of **30 reads** this approach may not be the most indicated if we are to expect multiple collisions.

There should be some study about realistic situations in order to get a more clear idea of how often collisions would happen in order to generate more realistic tests and obtain more realistic conclusions.

## Load results data
By running tests with Jmeter we can get the following results:

### 100 Writes
|           Label           | # Samples | Average |  Min |  Max  | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|:-------------------------:|:---------:|:-------:|:----:|:-----:|:---------:|:-------:|:----------:|:---------------:|:-----------:|:----------:|
|      Create Provider      |     1     |   1499  | 1499 |  1499 |     0     |  0.00%  |   0.66711  |       1.27      |     0.25    |    1954    |
|      Create Activity      |     1     |    44   |  44  |   44  |     0     |  0.00%  |  22.72727  |      47.32      |     9.08    |    2132    |
|        Create Offer       |    100    |    27   |  22  |   44  |    3.47   |  0.00%  |  35.88088  |      445.89     |     15.1    |   12725.2  |
|        Create Bank        |     1     |   1227  | 1227 |  1227 |     0     |  0.00%  |    0.815   |       1.23      |     0.27    |    1545    |
|       Create Client       |    101    |    18   |  11  |   63  |    6.49   |  0.00%  |   6.43394  |        13       |     2.61    |   2068.8   |
|       Create Account      |    100    |    23   |  18  |   98  |    8.54   |  0.00%  |  21.02165  |      186.97     |     7.7     |   9107.4   |
|      Deposit Account      |    100    |    22   |  16  |  101  |    9.19   |  0.00%  |  21.14165  |      35.94      |     8.48    |    1741    |
|        Create Hotel       |     1     |   1193  | 1193 |  1193 |     0     |  0.00%  |   0.83822  |       1.75      |     0.33    |    2143    |
|        Create Room        |    100    |    23   |  18  |   46  |    3.77   |  0.00%  |  41.78855  |      334.19     |    14.97    |   8189.1   |
|       Create Broker       |    100    |    32   |  13  |  1408 |   138.49  |  0.00%  |  13.95284  |      148.28     |     5.55    |   10882.2  |
|     Create Adventures     |    100    |    20   |  12  |   71  |    7.18   |  0.00%  |  17.46725  |      48.96      |     8.43    |   2870.1   |
|      Create RentACar      |     1     |   1108  | 1108 |  1108 |     0     |  0.00%  |   0.90253  |       1.83      |     0.32    |    2079    |
|         Create Car        |    100    |    27   |  21  |   58  |    4.74   |  0.00%  |  36.12717  |      509.34     |    15.98    |   14436.8  |
|      Create ItemType      |    100    |    31   |  15  |  1265 |    124    |  0.00%  |  30.85467  |       133       |     10.3    |   4413.8   |
|      Create TaxPayers     |    206    |    50   |  20  |  105  |   13.46   |  0.00%  |  19.62465  |      927.88     |     7.46    |   48416.3  |
|     Process Adventure     |    500    |   9225  |  29  | 62452 |  11510.09 |  14.00% |   5.31017  |      13.95      |     2.01    |   2689.7   |
| Clean Activities Database |     1     |    67   |  67  |   67  |     0     |  0.00%  |  14.92537  |      25.93      |     4.04    |    1779    |
|    Clean Banks Database   |     1     |   107   |  107 |  107  |     0     |  0.00%  |   9.34579  |      13.05      |     2.45    |    1430    |
|   Clean Hotels Database   |     1     |    56   |  56  |   56  |     0     |  0.00%  |  17.85714  |       35.3      |     4.71    |    2024    |
|   Clean Brokers Database  |     1     |    81   |  81  |   81  |     0     |  0.00%  |  12.34568  |       22.8      |     3.28    |    1891    |
|     Clean Tax Database    |     1     |   396   |  396 |  396  |     0     |  0.00%  |   2.52525  |       1.81      |     0.64    |     732    |
|    Clean Cars Database    |     1     |    54   |  54  |   54  |     0     |  0.00%  |  18.51852  |      32.77      |     4.99    |    1812    |
|           TOTAL           |    1618   |   2874  |  11  | 62452 |  7679.86  |  4.33%  |  12.07156  |      131.03     |     4.71    |   11114.7  |

### 100 Reads


|       Label       | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min | Max | Error % | Throughput |
|:-----------------:|:---------:|:-------:|:------:|:--------:|:--------:|:--------:|:---:|:---:|:-------:|:----------:|
|  Create Provider  |     1     |    36   |   36   |    36    |    36    |    36    |  36 |  36 |  0.00%  |  27.77778  |
|  Create Activity  |     1     |    19   |   19   |    19    |    19    |    19    |  19 |  19 |  0.00%  |  52.63158  |
|    Create Offer   |     1     |    12   |   12   |    12    |    12    |    12    |  12 |  12 |  0.00%  |  83.33333  |
|    Create Bank    |     1     |    25   |   25   |    25    |    25    |    25    |  25 |  25 |  0.00%  |     40     |
|   Create Client   |    101    |    11   |   11   |    15    |    16    |    19    |  9  |  20 |  0.00%  |  11.63192  |
|   Create Account  |    100    |    14   |   13   |    17    |    20    |    45    |  11 |  70 |  0.00%  |  32.36246  |
|  Deposit Account  |    100    |    15   |   14   |    20    |    21    |    24    |  10 |  62 |  0.00%  |  32.41491  |
|    Create Hotel   |     1     |    28   |   28   |    28    |    28    |    28    |  28 |  28 |  0.00%  |  35.71429  |
|    Create Room    |    100    |    14   |   13   |    18    |    20    |    24    |  10 |  55 |  0.00%  |  67.61325  |
|   Create Broker   |    100    |    13   |   12   |    17    |    20    |    23    |  9  |  66 |  0.00%  |  24.73411  |
| Create Adventures |    100    |    14   |   13   |    18    |    19    |    22    |  10 |  60 |  0.00%  |  24.78929  |
|  Create RentACar  |     1     |    19   |   19   |    19    |    19    |    19    |  19 |  19 |  0.00%  |  52.63158  |
|     Create Car    |    100    |    17   |   16   |    20    |    21    |    44    |  13 |  65 |  0.00%  |  57.63689  |
|  Create ItemType  |    100    |    12   |   12   |    16    |    18    |    19    |  10 |  20 |  0.00%  |  78.80221  |
|  Create TaxPayers |    100    |    26   |   26   |    37    |    40    |    51    |  12 |  52 |  0.00%  |  37.66478  |
|    Read Brokers   |    2000   |    30   |    9   |    65    |    165   |    392   |  5  | 601 |  0.00%  |  24.15342  |
|  Read Adventures  |    2000   |    21   |    4   |    53    |    112   |    323   |  2  | 514 |  0.00%  |  24.15488  |
|     Read Banks    |    2000   |    26   |    4   |    79    |    137   |    325   |  2  | 487 |  0.00%  |  24.15663  |
|    Read Clients   |    2000   |    24   |    9   |    65    |    128   |    218   |  4  | 370 |  0.00%  |  24.15751  |
|   Read Accounts   |    2000   |    27   |   10   |    81    |    138   |    203   |  4  | 290 |  0.00%  |  24.15867  |
|    Read Hotels    |    2000   |    24   |    4   |    84    |    138   |    268   |  2  | 375 |  0.00%  |  24.16276  |
|     Read Rooms    |    2000   |    23   |    9   |    60    |    110   |    224   |  4  | 318 |  0.00%  |  24.16422  |
|   Read Providers  |    2000   |    29   |    5   |    96    |    185   |    284   |  2  | 444 |  0.00%  |  24.16743  |

## 30 Writes
|           Label           | # Samples | Average |  Min |  Max  | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|:-------------------------:|:---------:|:-------:|:----:|:-----:|:---------:|:-------:|:----------:|:---------------:|:-----------:|:----------:|
|      Create Provider      |     1     |   1499  | 1499 |  1499 |     0     |  0.00%  |   0.66711  |       1.27      |     0.25    |    1954    |
|      Create Activity      |     1     |    44   |  44  |   44  |     0     |  0.00%  |  22.72727  |      47.32      |     9.08    |    2132    |
|        Create Offer       |    100    |    27   |  22  |   44  |    3.47   |  0.00%  |  35.88088  |      445.89     |     15.1    |   12725.2  |
|        Create Bank        |     1     |   1227  | 1227 |  1227 |     0     |  0.00%  |    0.815   |       1.23      |     0.27    |    1545    |
|       Create Client       |    101    |    18   |  11  |   63  |    6.49   |  0.00%  |   6.43394  |        13       |     2.61    |   2068.8   |
|       Create Account      |    100    |    23   |  18  |   98  |    8.54   |  0.00%  |  21.02165  |      186.97     |     7.7     |   9107.4   |
|      Deposit Account      |    100    |    22   |  16  |  101  |    9.19   |  0.00%  |  21.14165  |      35.94      |     8.48    |    1741    |
|        Create Hotel       |     1     |   1193  | 1193 |  1193 |     0     |  0.00%  |   0.83822  |       1.75      |     0.33    |    2143    |
|        Create Room        |    100    |    23   |  18  |   46  |    3.77   |  0.00%  |  41.78855  |      334.19     |    14.97    |   8189.1   |
|       Create Broker       |    100    |    32   |  13  |  1408 |   138.49  |  0.00%  |  13.95284  |      148.28     |     5.55    |   10882.2  |
|     Create Adventures     |    100    |    20   |  12  |   71  |    7.18   |  0.00%  |  17.46725  |      48.96      |     8.43    |   2870.1   |
|      Create RentACar      |     1     |   1108  | 1108 |  1108 |     0     |  0.00%  |   0.90253  |       1.83      |     0.32    |    2079    |
|         Create Car        |    100    |    27   |  21  |   58  |    4.74   |  0.00%  |  36.12717  |      509.34     |    15.98    |   14436.8  |
|      Create ItemType      |    100    |    31   |  15  |  1265 |    124    |  0.00%  |  30.85467  |       133       |     10.3    |   4413.8   |
|      Create TaxPayers     |    206    |    50   |  20  |  105  |   13.46   |  0.00%  |  19.62465  |      927.88     |     7.46    |   48416.3  |
|     Process Adventure     |    500    |   9225  |  29  | 62452 |  11510.09 |  14.00% |   5.31017  |      13.95      |     2.01    |   2689.7   |
| Clean Activities Database |     1     |    67   |  67  |   67  |     0     |  0.00%  |  14.92537  |      25.93      |     4.04    |    1779    |
|    Clean Banks Database   |     1     |   107   |  107 |  107  |     0     |  0.00%  |   9.34579  |      13.05      |     2.45    |    1430    |
|   Clean Hotels Database   |     1     |    56   |  56  |   56  |     0     |  0.00%  |  17.85714  |       35.3      |     4.71    |    2024    |
|   Clean Brokers Database  |     1     |    81   |  81  |   81  |     0     |  0.00%  |  12.34568  |       22.8      |     3.28    |    1891    |
|     Clean Tax Database    |     1     |   396   |  396 |  396  |     0     |  0.00%  |   2.52525  |       1.81      |     0.64    |     732    |
|    Clean Cars Database    |     1     |    54   |  54  |   54  |     0     |  0.00%  |  18.51852  |      32.77      |     4.99    |    1812    |
|           TOTAL           |    1618   |   2874  |  11  | 62452 |  7679.86  |  4.33%  |  12.07156  |      131.03     |     4.71    |   11114.7  |


