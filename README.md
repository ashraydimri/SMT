# SMT
Stock Market Tool

The ‘Stock Market Tool’ will provide a way to view and analyze the stock quotes of any company listed in the Bombay Stock Exchange. The 
main aim of the project is the development of a software which a consumer uses for getting latest news, historical data, stock quotes et.

The major feature of this project is usage of popular open source relational database management system MySQL and web page scraping.The project will result in ease of day to day operations in the life of a stock market analyst or enthusiast as one gets latest quotes and indices on the go. It improves the reliability of the data provided and decreases the consumption of time for the user.

The main functionalities are portfolio analysis in which the user can see a statistical analysis of the companies provided in his/her portfolio. This allows him to compare the probability of a company stock rising the day after it falls. Also BSE close prediction has been implemented by using MLR(Multiple Linear Regression) on the basis of today's news, open, high and close values. The market news is analysed from a group of positive and negative words and then given a rating. The close predicted gives -25 to +25 difference to the actual close on average. All other information and the database was directly taken from Yahoo URLs. 

Because the language used to build this software is Java (JDK 8), this software can be used on any platform in the world. The program uses a custom made back-end for getting news, stocks etc. from the internet. Also since the product is integrated with MySQL the program uses JDBC connector package for connecting with the MySQL server. This makes the data stored accessible anywhere in the world when the server is hosted on the internet. For performing MLR and matrix operations the code was imported from ::

https://github.com/bartoszkopinski/wap/tree/master/wap_project/Mathematics/src/matrix

Other important links important in understanding the project are ::

Yahoo Finance news rss :: https://in.news.yahoo.com/rss/markets

MLR implementation in java :: http://www.codeproject.com/Articles/566326/Multi-Linear-Regression-in-Java

Yahoo finance India :: https://in.finance.yahoo.com/

For MySQL :: https://www.mysql.com/
