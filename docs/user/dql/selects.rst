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

Alias
=====

::

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
|           32838|           Nanette|            Bates|
+----------------+------------------+-----------------+
|           39225|             Amber|             Duke|
+----------------+------------------+-----------------+
|            5686|            Hattie|             Bond|
+----------------+------------------+-----------------+
|            4180|              Dale|            Adams|
+----------------+------------------+-----------------+


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
|           32838|           Nanette|            Bates|
+----------------+------------------+-----------------+
|           39225|             Amber|             Duke|
+----------------+------------------+-----------------+
|            5686|            Hattie|             Bond|
+----------------+------------------+-----------------+
|            4180|              Dale|            Adams|
+----------------+------------------+-----------------+


Group By
========

GROUP BY clause can be used to aggregate result of WHERE on some field(s)::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT state, AVG(balance) FROM accounts GROUP BY state"
	}

	POST /_opendistro/_sql/_explain
	{
	  "query": "SELECT state, AVG(balance) FROM accounts GROUP BY state"
	}
	{
	  "from" : 0,
	  "size" : 0,
	  "_source" : {
	    "includes" : [
	      "state",
	      "AVG"
	    ],
	    "excludes" : [ ]
	  },
	  "stored_fields" : "state",
	  "aggregations" : {
	    "state.keyword" : {
	      "terms" : {
	        "field" : "state.keyword",
	        "size" : 200,
	        "min_doc_count" : 1,
	        "shard_min_doc_count" : 0,
	        "show_term_doc_count_error" : false,
	        "order" : [
	          {
	            "_count" : "desc"
	          },
	          {
	            "_key" : "asc"
	          }
	        ]
	      },
	      "aggregations" : {
	        "AVG(balance)" : {
	          "avg" : {
	            "field" : "balance"
	          }
	        }
	      }
	    }
	  }
	}

+-------------------------+-----------------------+
|  state.keyword (keyword)|  AVG(balance) (double)|
+=========================+=======================+
|                       IL|                39225.0|
+-------------------------+-----------------------+
|                       MD|                 4180.0|
+-------------------------+-----------------------+
|                       TN|                 5686.0|
+-------------------------+-----------------------+
|                       VA|                32838.0|
+-------------------------+-----------------------+


Group By
========

GROUP BY clause can be used to aggregate result of WHERE on some field(s)::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT state, AVG(balance) AS avg FROM accounts GROUP BY state HAVING avg > 10000"
	}

	POST /_opendistro/_sql/_explain
	{
	  "query": "SELECT state, AVG(balance) AS avg FROM accounts GROUP BY state HAVING avg > 10000"
	}
	{
	  "from" : 0,
	  "size" : 0,
	  "_source" : {
	    "includes" : [
	      "state",
	      "AVG"
	    ],
	    "excludes" : [ ]
	  },
	  "stored_fields" : "state",
	  "aggregations" : {
	    "state.keyword" : {
	      "terms" : {
	        "field" : "state.keyword",
	        "size" : 200,
	        "min_doc_count" : 1,
	        "shard_min_doc_count" : 0,
	        "show_term_doc_count_error" : false,
	        "order" : [
	          {
	            "_count" : "desc"
	          },
	          {
	            "_key" : "asc"
	          }
	        ]
	      },
	      "aggregations" : {
	        "avg" : {
	          "avg" : {
	            "field" : "balance"
	          }
	        },
	        "bucket_filter" : {
	          "bucket_selector" : {
	            "buckets_path" : {
	              "avg" : "avg"
	            },
	            "script" : {
	              "source" : "params.avg > 10000",
	              "lang" : "painless"
	            },
	            "gap_policy" : "skip"
	          }
	        }
	      }
	    }
	  }
	}

+-------------------------+--------------+
|  state.keyword (keyword)|  avg (double)|
+=========================+==============+
|                       IL|       39225.0|
+-------------------------+--------------+
|                       VA|       32838.0|
+-------------------------+--------------+


Having
======

HAVING clause can help filter the result of GROUP BY::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT state, AVG(balance) AS avg FROM accounts GROUP BY state HAVING avg > 10000"
	}

	POST /_opendistro/_sql/_explain
	{
	  "query": "SELECT state, AVG(balance) AS avg FROM accounts GROUP BY state HAVING avg > 10000"
	}
	{
	  "from" : 0,
	  "size" : 0,
	  "_source" : {
	    "includes" : [
	      "state",
	      "AVG"
	    ],
	    "excludes" : [ ]
	  },
	  "stored_fields" : "state",
	  "aggregations" : {
	    "state.keyword" : {
	      "terms" : {
	        "field" : "state.keyword",
	        "size" : 200,
	        "min_doc_count" : 1,
	        "shard_min_doc_count" : 0,
	        "show_term_doc_count_error" : false,
	        "order" : [
	          {
	            "_count" : "desc"
	          },
	          {
	            "_key" : "asc"
	          }
	        ]
	      },
	      "aggregations" : {
	        "avg" : {
	          "avg" : {
	            "field" : "balance"
	          }
	        },
	        "bucket_filter" : {
	          "bucket_selector" : {
	            "buckets_path" : {
	              "avg" : "avg"
	            },
	            "script" : {
	              "source" : "params.avg > 10000",
	              "lang" : "painless"
	            },
	            "gap_policy" : "skip"
	          }
	        }
	      }
	    }
	  }
	}

+-------------------------+--------------+
|  state.keyword (keyword)|  avg (double)|
+=========================+==============+
|                       IL|       39225.0|
+-------------------------+--------------+
|                       VA|       32838.0|
+-------------------------+--------------+


Where
=====

WHERE clause can filter out the result set based on conditions::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT balance, firstname, lastname FROM accounts WHERE balance > 10000"
	}

	POST /_opendistro/_sql/_explain
	{
	  "query": "SELECT balance, firstname, lastname FROM accounts WHERE balance > 10000"
	}
	{
	  "from" : 0,
	  "size" : 200,
	  "query" : {
	    "bool" : {
	      "filter" : [
	        {
	          "bool" : {
	            "must" : [
	              {
	                "range" : {
	                  "balance" : {
	                    "from" : 10000,
	                    "to" : null,
	                    "include_lower" : false,
	                    "include_upper" : true,
	                    "boost" : 1.0
	                  }
	                }
	              }
	            ],
	            "adjust_pure_negative" : true,
	            "boost" : 1.0
	          }
	        }
	      ],
	      "adjust_pure_negative" : true,
	      "boost" : 1.0
	    }
	  },
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
|           32838|           Nanette|            Bates|
+----------------+------------------+-----------------+
|           39225|             Amber|             Duke|
+----------------+------------------+-----------------+


