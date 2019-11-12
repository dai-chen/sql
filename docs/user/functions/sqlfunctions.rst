.. highlight:: sh

=============
SQL Functions
=============

Introduction
============

In SQL standard, many useful functions are provided and built in database implementations to help user ...

ABS
===

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ABS(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ABS_1" : {
	      "script" : {
	        "source" : "def abs_1 = Math.abs(3);return abs_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+----------------+
	|  ABS_1 (double)|
	+================+
	|               3|
	+----------------+
	

ASCII
=====

Description
-----------

null

Syntax
------

Specifications: (STRING T) -> T

Semantics:

1. The function accepts an argument of type STRING as string.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ASCII('hello world') FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ASCII_1" : {
	      "script" : {
	        "source" : "def ascii_1 = (int) 'hello world'.charAt(0);return ascii_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+-------------------+
	|  ASCII_1 (integer)|
	+===================+
	|                104|
	+-------------------+
	

ATAN
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ATAN(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ATAN_1" : {
	      "script" : {
	        "source" : "def atan_1 = Math.atan(3);return atan_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|     ATAN_1 (double)|
	+====================+
	|  1.2490457723982544|
	+--------------------+
	

ATAN2
=====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T, NUMBER) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number and an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ATAN2(3, 3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ATAN2_1" : {
	      "script" : {
	        "source" : "def atan2_1 = Math.atan2(3, 3);return atan2_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|    ATAN2_1 (double)|
	+====================+
	|  0.7853981633974483|
	+--------------------+
	

CBRT
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT CBRT(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "CBRT_1" : {
	      "script" : {
	        "source" : "def cbrt_1 = Math.cbrt(3);return cbrt_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|     CBRT_1 (double)|
	+====================+
	|  1.4422495703074083|
	+--------------------+
	

CEIL
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT CEIL(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "CEIL_1" : {
	      "script" : {
	        "source" : "def ceil_1 = Math.ceil(3);return ceil_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+-----------------+
	|  CEIL_1 (double)|
	+=================+
	|                3|
	+-----------------+
	

CONCAT
======

Description
-----------

null

Syntax
------

Specifications: 

Semantics:



CONCAT_WS
=========

Description
-----------

null

Syntax
------

Specifications: 

Semantics:



COS
===

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT COS(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "COS_1" : {
	      "script" : {
	        "source" : "def cos_1 = Math.cos(3);return cos_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+---------------------+
	|       COS_1 (double)|
	+=====================+
	|  -0.9899924966004454|
	+---------------------+
	

COSH
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT COSH(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "COSH_1" : {
	      "script" : {
	        "source" : "def cosh_1 = Math.cosh(3);return cosh_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|     COSH_1 (double)|
	+====================+
	|  10.067661995777765|
	+--------------------+
	

COT
===

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT COT(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "COT_1" : {
	      "script" : {
	        "source" : "def cot_1 = 1 / Math.tan(3);return cot_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|      COT_1 (double)|
	+====================+
	|  -7.015252551434534|
	+--------------------+
	

DATE_FORMAT
===========

Description
-----------

null

Syntax
------

Specifications: 

Semantics:



DEGREES
=======

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT DEGREES(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "DEGREES_1" : {
	      "script" : {
	        "source" : "def degrees_1 = Math.toDegrees(3);return degrees_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|  DEGREES_1 (double)|
	+====================+
	|  171.88733853924697|
	+--------------------+
	

E
=

Description
-----------

null

Syntax
------

Specifications: () -> DOUBLE

Semantics:

1. The function accepts .


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT E() FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "E_1" : {
	      "script" : {
	        "source" : "def E_2 = Math.E;return E_2;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+-------------------+
	|       E_1 (double)|
	+===================+
	|  2.718281828459045|
	+-------------------+
	

EXP
===

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT EXP(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "EXP_1" : {
	      "script" : {
	        "source" : "def exp_1 = Math.exp(3);return exp_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|      EXP_1 (double)|
	+====================+
	|  20.085536923187668|
	+--------------------+
	

EXPM1
=====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT EXPM1(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "EXPM1_1" : {
	      "script" : {
	        "source" : "def expm1_1 = Math.expm1(3);return expm1_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|    EXPM1_1 (double)|
	+====================+
	|  19.085536923187668|
	+--------------------+
	

FLOOR
=====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT FLOOR(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "FLOOR_1" : {
	      "script" : {
	        "source" : "def floor_1 = Math.floor(3);return floor_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+------------------+
	|  FLOOR_1 (double)|
	+==================+
	|                 3|
	+------------------+
	

LENGTH
======

Description
-----------

null

Syntax
------

Specifications: (STRING) -> INTEGER

Semantics:

1. The function accepts an argument of type STRING as string.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LENGTH('hello world') FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LENGTH_1" : {
	      "script" : {
	        "source" : "def length_1 = 'hello world'.length();return length_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|  LENGTH_1 (integer)|
	+====================+
	|                  11|
	+--------------------+
	

LOCATE
======

Description
-----------

null

Syntax
------

Specifications: (STRING, STRING, INTEGER) -> INTEGER, (STRING, STRING) -> INTEGER

Semantics:

1. The function accepts an argument of type STRING as string and an argument of type STRING as string and an argument of type INTEGER as integer.
2. The function accepts an argument of type STRING as string and an argument of type STRING as string.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOCATE('hello world', 'hello world', 3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOCATE_1" : {
	      "script" : {
	        "source" : "def locate_1 = 'hello world'.indexOf('hello world',2)+1;return locate_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|  LOCATE_1 (integer)|
	+====================+
	|                   0|
	+--------------------+
	

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOCATE('hello world', 'hello world') FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOCATE_1" : {
	      "script" : {
	        "source" : "def locate_1 = 'hello world'.indexOf('hello world',0)+1;return locate_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|  LOCATE_1 (integer)|
	+====================+
	|                   1|
	+--------------------+
	

LOG
===

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T, (NUMBER T, NUMBER) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.
2. The function accepts an argument of type NUMBER as number and an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOG(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOG_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(3)/Math.log(Math.E);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|      LOG_1 (double)|
	+====================+
	|  1.0986122886681098|
	+--------------------+
	

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOG(3, 3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOG_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(3)/Math.log(3);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+----------------+
	|  LOG_1 (double)|
	+================+
	|               1|
	+----------------+
	

LOG2
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOG2(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOG2_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(3)/Math.log(2);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|     LOG2_1 (double)|
	+====================+
	|  1.5849625007211563|
	+--------------------+
	

LOG10
=====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LOG10(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LOG10_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(3)/Math.log(10);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+---------------------+
	|     LOG10_1 (double)|
	+=====================+
	|  0.47712125471966244|
	+---------------------+
	

LN
==

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LN(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LN_1" : {
	      "script" : {
	        "source" : "def log_1 = Math.log(3)/Math.log(Math.E);return log_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|       LN_1 (double)|
	+====================+
	|  1.0986122886681098|
	+--------------------+
	

LOWER
=====

Description
-----------

null

Syntax
------

Specifications: 

Semantics:



LTRIM
=====

Description
-----------

null

Syntax
------

Specifications: (STRING T) -> T

Semantics:

1. The function accepts an argument of type STRING as string.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT LTRIM('hello world') FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "LTRIM_1" : {
	      "script" : {
	        "source" : "int pos=0;while(pos < 'hello world'.length() && Character.isWhitespace('hello world'.charAt(pos))) {pos ++;} def ltrim_1 = 'hello world'.substring(pos, 'hello world'.length());return ltrim_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+----------------+
	|  LTRIM_1 (text)|
	+================+
	|     hello world|
	+----------------+
	

PI
==

Description
-----------

null

Syntax
------

Specifications: () -> DOUBLE

Semantics:

1. The function accepts .


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT PI() FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "PI_1" : {
	      "script" : {
	        "source" : "def PI_2 = Math.PI;return PI_2;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+-------------------+
	|      PI_1 (double)|
	+===================+
	|  3.141592653589793|
	+-------------------+
	

POW
===

Description
-----------

null

Syntax
------

Specifications: 

Semantics:



POWER
=====

Description
-----------

null

Syntax
------

Specifications: 

Semantics:



RADIANS
=======

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT RADIANS(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "RADIANS_1" : {
	      "script" : {
	        "source" : "def radians_1 = Math.toRadians(3);return radians_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+---------------------+
	|   RADIANS_1 (double)|
	+=====================+
	|  0.05235987755982989|
	+---------------------+
	

REPLACE
=======

Description
-----------

null

Syntax
------

Specifications: (STRING T, STRING, STRING) -> T

Semantics:

1. The function accepts an argument of type STRING as string and an argument of type STRING as string and an argument of type STRING as string.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT REPLACE('hello world', 'hello world', 'hello world') FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "REPLACE_1" : {
	      "script" : {
	        "source" : "def replace_1 = 'hello world'.replace('hello world','hello world');return replace_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+------------------+
	|  REPLACE_1 (text)|
	+==================+
	|       hello world|
	+------------------+
	

RINT
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT RINT(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "RINT_1" : {
	      "script" : {
	        "source" : "def rint_1 = Math.rint(3);return rint_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+-----------------+
	|  RINT_1 (double)|
	+=================+
	|                3|
	+-----------------+
	

ROUND
=====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT ROUND(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "ROUND_1" : {
	      "script" : {
	        "source" : "def round_1 = Math.round(3);return round_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+------------------+
	|  ROUND_1 (double)|
	+==================+
	|                 3|
	+------------------+
	

RTRIM
=====

Description
-----------

null

Syntax
------

Specifications: (STRING T) -> T

Semantics:

1. The function accepts an argument of type STRING as string.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT RTRIM('hello world') FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "RTRIM_1" : {
	      "script" : {
	        "source" : "int pos='hello world'.length()-1;while(pos >= 0 && Character.isWhitespace('hello world'.charAt(pos))) {pos --;} def rtrim_1 = 'hello world'.substring(0, pos+1);return rtrim_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+----------------+
	|  RTRIM_1 (text)|
	+================+
	|     hello world|
	+----------------+
	

SIGN
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SIGN(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SIGN_1" : {
	      "script" : {
	        "source" : "def signum_1 = Math.signum(3);return signum_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+-----------------+
	|  SIGN_1 (double)|
	+=================+
	|                1|
	+-----------------+
	

SIGNUM
======

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SIGNUM(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SIGNUM_1" : {
	      "script" : {
	        "source" : "def signum_1 = Math.signum(3);return signum_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+-------------------+
	|  SIGNUM_1 (double)|
	+===================+
	|                  1|
	+-------------------+
	

SIN
===

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SIN(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SIN_1" : {
	      "script" : {
	        "source" : "def sin_1 = Math.sin(3);return sin_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|      SIN_1 (double)|
	+====================+
	|  0.1411200080598672|
	+--------------------+
	

SINH
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SINH(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SINH_1" : {
	      "script" : {
	        "source" : "def sinh_1 = Math.sinh(3);return sinh_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|     SINH_1 (double)|
	+====================+
	|  10.017874927409903|
	+--------------------+
	

SQRT
====

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SQRT(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SQRT_1" : {
	      "script" : {
	        "source" : "def sqrt_1 = Math.sqrt(3);return sqrt_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|     SQRT_1 (double)|
	+====================+
	|  1.7320508075688772|
	+--------------------+
	

SUBSTRING
=========

Description
-----------

The SUBSTRING() function extracts a substring from a string.

Syntax
------

Specifications: (STRING T, INTEGER, INTEGER) -> T

Semantics:

1. The function accepts an argument of type STRING as string and an argument of type INTEGER as start position and an argument of type INTEGER as length.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT SUBSTRING('hello world', 3, 3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "SUBSTRING_1" : {
	      "script" : {
	        "source" : "def substring_1 = 'hello world'.substring(2, 5);return substring_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+--------------------+
	|  SUBSTRING_1 (text)|
	+====================+
	|                 llo|
	+--------------------+
	

TAN
===

Description
-----------

null

Syntax
------

Specifications: (NUMBER T) -> T

Semantics:

1. The function accepts an argument of type NUMBER as number.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT TAN(3) FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "TAN_1" : {
	      "script" : {
	        "source" : "def tan_1 = Math.tan(3);return tan_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+---------------------+
	|       TAN_1 (double)|
	+=====================+
	|  -0.1425465430742778|
	+---------------------+
	

UPPER
=====

Description
-----------

null

Syntax
------

Specifications: 

Semantics:



YEAR
====

Description
-----------

null

Syntax
------

Specifications: (DATE) -> INTEGER

Semantics:

1. The function accepts an argument of type DATE as date.


Examples
--------

SQL query::

	POST /_opendistro/_sql?format=jdbc
	{
	  "query": "SELECT YEAR('2019-11-09') FROM accounts LIMIT 1"
	}

Explain::

	{
	  "from" : 0,
	  "size" : 1,
	  "_source" : {
	    "includes" : [ ],
	    "excludes" : [ ]
	  },
	  "script_fields" : {
	    "YEAR_1" : {
	      "script" : {
	        "source" : "def year_1 = doc['2019-11-09'].value.year;return year_1;",
	        "lang" : "painless"
	      },
	      "ignore_failure" : false
	    }
	  }
	}

Result set::

	+---------------+
	|  YEAR_1 (text)|
	+===============+
	

