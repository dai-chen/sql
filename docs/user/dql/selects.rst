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

Select
======

Description
-----------

SELECT and FROM clause are basic part of query to specify which fields from which index to fetch Optional you can alias index or field name for clarity and renaming purpose.  In SQL standard, full table name can also be used as prefix if table alias not present.  In both cases, table name and table alias are optional.

Syntax
------

SELECT [DISTINCT] items FROM index [WHERE conditions]

Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT balance, firstname, lastname FROM accounts"
	}

Explain::

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

Result set::

	+----------------+------------------+-----------------+
	|  balance (long)|  firstname (text)|  lastname (text)|
	+================+==================+=================+
	|           39225|             Amber|             Duke|
	+----------------+------------------+-----------------+
	|            5686|            Hattie|             Bond|
	+----------------+------------------+-----------------+
	|           32838|           Nanette|            Bates|
	+----------------+------------------+-----------------+
	|            4180|              Dale|            Adams|
	+----------------+------------------+-----------------+
	

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT a.balance AS bal, a.firstname AS first, a.lastname AS last FROM accounts a"
	}

Explain::

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

Result set::

	+----------------+------------------+-----------------+
	|  balance (long)|  firstname (text)|  lastname (text)|
	+================+==================+=================+
	|           39225|             Amber|             Duke|
	+----------------+------------------+-----------------+
	|            5686|            Hattie|             Bond|
	+----------------+------------------+-----------------+
	|           32838|           Nanette|            Bates|
	+----------------+------------------+-----------------+
	|            4180|              Dale|            Adams|
	+----------------+------------------+-----------------+
	

