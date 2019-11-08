.. highlight:: sh

=======
SELECTS
=======

.. rubric:: Table of contents

.. contents::
   :local:


Introduction
============

Select query is the most common type of query to fetch data from database. It is very simple and powerful along with other clause such as WHERE, GROUP BY, HAVING, ORDER BY and LIMIT. We are going to demonstrate with example how they can be used in real world.

Select Basics
=============

SELECT and FROM clause are basic part of query to specify which fields from which index to fetch::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT balance, firstname, lastname FROM accounts"
	}

	POST /_opendistro/_sql/_explain
	{
	  "query": "SELECT balance, firstname, lastname FROM accounts"
	}
	{
	  "from" : 0,
	  "size" : 200,
	  "_source" : {
	    "includes" : [
	      "balance",
	      "firstname",
	      "lastname"
	    ],
	    "excludes" : [ ]
	  }
	}

+----------------+------------------+-----------------+
|  balance (long)|  firstname (text)|  lastname (text)|
+================+==================+=================+
|           39225|             Amber|             Duke|
+----------------+------------------+-----------------+
|            5686|            Hattie|             Bond|
+----------------+------------------+-----------------+
|            4180|              Dale|            Adams|
+----------------+------------------+-----------------+
|           32838|           Nanette|            Bates|
+----------------+------------------+-----------------+


