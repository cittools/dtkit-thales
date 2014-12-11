/*******************************************************************************
 * Copyright (c) 2011 Thales Corporate Services SAS                             *
 * Author : Aravindan Mahendran                                                 *
 *                                                                              *
 * The MIT license                                                              *
 *                                                                              *
 * Permission is hereby granted, free of charge, to any person obtaining a copy *
 * of this software and associated documentation files (the "Software"), to deal*
 * in the Software without restriction, including without limitation the rights *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell    *
 * copies of the Software, and to permit persons to whom the Software is        *
 * furnished to do so, subject to the following conditions:                     *
 *                                                                              *
 * The above copyright notice and this permission notice shall be included in   *
 * all copies or substantial portions of the Software.                          *
 *                                                                              *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR   *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,     *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER       *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,*
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN    *
 * THE SOFTWARE.                                                                *
 *******************************************************************************/

package com.thalesgroup.dtkit.tusar.prqa;

public class PRQAConstants {
	
	/**
	 * Common
	 */
	public static enum PRQAComponentType {
		QACPP_FILE, QACPP_CLASS, QACPP_CLASS2, QACPP_FUNCTION, QAC_PROJECT, QAC_FILE, QAC_FUNCTION, FILE, FUNCTION, UNKNOWN
	}
	
	/**
	 * QACPP
	 */
	
	//file metrics
	public static final String STTPP = "STTPP"; //Total unpreprocessed source lines
	public static final String STTLN = "STTLN"; //Total preprocessed code lines
	public static final String STCCA = "STCCA"; //Total Number of Characters
	public static final String STCCB = "STCCB"; //Total Number of Code Characters
	public static final String STCCC = "STCCC"; //Total Number of Comment Characters
	public static final String STCDN = "STCDN"; //Comment to code ratio
	public static final String STTOT = "STTOT"; //Total number of tokens used
	public static final String STVAR = "STVAR"; //Number of identifiers
	public static final String STOPT = "STOPT"; //Halstead distinct operators
	public static final String STOPN = "STOPN"; //Halstead distinct operands
					
	//class metrics
	public static final String STDIT = "STDIT"; //Depth of Inheritance
	public static final String STNOP = "STNOP"; //Number of immediate children
	public static final String STMTH = "STMTH"; //Number of methods available in class
	
	//class metrics 2
	public static final String STNOC = "STNOC"; //Depth of Inheritance
	public static final String STWMC = "STWMC"; //Number of immediate children
	public static final String STRFC = "STRFC"; //Number of methods available in class
	public static final String STCBO = "STCBO"; //Depth of Inheritance
	public static final String STLCM = "STLCM"; //Number of immediate children
	
	//function metrics
	public static final String STSUB = "STSUB"; //Number of Function Calls
	public static final String STXLN = "STXLN"; //Number of executable lines
	public static final String STGTO = "STGTO"; //Number of goto statements
	public static final String STCYC = "STCYC"; //Cyclomatic complexity
	public static final String STPAR = "STPAR"; //Number of parameters
	public static final String STPTH = "STPTH"; //Estimated static path count
	public static final String STMIF = "STMIF"; //Maximum nesting of control structures
	public static final String STLIN = "STLIN"; //Number of maintainable lines of code

	//File and components
	public static final String STFIL = "STFIL"; //File name
	public static final String STNAM = "STNAM"; //File, class or function name
	
	
	//Columns for the violations
	public static final int LINE_INDEX = 0;
	public static final int SEVERITY_INDEX = 1;
	public static final int SEVERITY = 2;
	public static final int MESSAGE_INDEX = 3;
	public static final int MESSAGE = 4;
	
	
	
	
	/**
	 * QAC
	 */
	//Function based metrics
	public static final String STAKI ="STAKI"; //Akiyama's Criterion
	public static final String STAV1 ="STAV1"; //Average Size of Function Statements (variant 1)
	public static final String STAV2 ="STAV2"; //Average Size of Function Statements (variant 2)
	public static final String STAV3 ="STAV3"; //Average Size of Function Statements (variant 3)
	public static final String STBAK ="STBAK"; //Number of Backward Jumps
	public static final String STCAL ="STCAL"; //Number of Distinct Function Calls
	//public static final String STCYC ="STCYC"; //Cyclomatic Complexity
	public static final String STELF ="STELF"; //Number of Dangling Else-Ifs
	public static final String STFN1 ="STFN1"; //Number of Function Operator Occurrences
	public static final String STFN2 ="STFN2"; //Number of Function Operand Occurrences
	//public static final String STGTO ="STGTO"; //Number of Goto statements
	public static final String STKDN ="STKDN"; //Knot Density
	public static final String STKNT ="STKNT"; //Knot Count
	public static final String STLCT ="STLCT"; //Number of Local Variables Declared
	//public static final String STLIN ="STLIN"; //Number of Maintainable Code Lines
	public static final String STLOP ="STLOP"; //Number of Logical Operators
	public static final String STM07 ="STM07"; //Essential Cyclomatic Complexity
	public static final String STM19 ="STM19"; //Number of Exit Points
	public static final String STM29 ="STM29"; //Number of Functions Calling this Function
	public static final String STMCC ="STMCC"; //Myer?s Interval
	//public static final String STMIF ="STMIF"; //Maximum Nesting of Control Structures
	//public static final String STPAR ="STPAR"; //Number of Function Parameters
	public static final String STPBG ="STPBG"; //Path-Based Residual Bug Estimate
	public static final String STPDN ="STPDN"; //Path Density
	//public static final String STPTH ="STPTH"; //Estimated Static Path Count
	public static final String STRET ="STRET"; //Number of Function Return Points
	public static final String STST1 ="STST1"; //Number of Statements in Function (variant 1)
	public static final String STST2 ="STST2"; //Number of Statements in Function (variant 2)
	public static final String STST3 ="STST3"; //Number of Statements in Function (variant 3)
	//public static final String STSUB ="STSUB"; //Number of Function Calls
	public static final String STUNR ="STUNR"; //Number of Unreachable Statements
	public static final String STUNV ="STUNV"; //Number of Unused and Non-Reused Variables
	//public static final String STXLN ="STXLN"; //Number of Executable Lines
	
	
	
	
	
	//File Based Metrics
	public static final String STBME ="STBME"; //COCOMO Embedded Programmer Months
	public static final String STBMO ="STBMO"; //COCOMO Organic Programmer Months
	public static final String STBMS ="STBMS"; //COCOMO Semi-detached Programmer Months
	public static final String STBUG ="STBUG"; //Residual Bugs (token-based estimate)
	//public static final String STCDN ="STCDN"; //Comment to Code Ratio
	public static final String STDEV ="STDEV"; //Estimated Development Time
	public static final String STDIF ="STDIF"; //Program Difficulty
	public static final String STECT ="STECT"; //Number of External Variables
	public static final String STEFF ="STEFF"; //Program Effort
	public static final String STFCO ="STFCO"; //Estimated Function Coupling
	public static final String STFNC ="STFNC"; //Number of Function Definitions
	public static final String STHAL ="STHAL"; //Halstead Prediction Of STTOT
	public static final String STM20 ="STM20"; //Number of Operand Occurrences
	public static final String STM21 ="STM21"; //Number of Operator Occurrences
	public static final String STM22 ="STM22"; //Number of Statements
	public static final String STM28 ="STM28"; //Number of Non-Header Comments
	public static final String STM33 ="STM33"; //Number of Internal Comments
	public static final String STMOB ="STMOB"; //Code Mobility
	//public static final String STOPN ="STOPN"; //Halstead Distinct Operands
	//public static final String STOPT ="STOPT"; //Halstead Distinct Operators
	public static final String STPRT ="STPRT"; //Estimated Porting Time
	public static final String STSCT ="STSCT"; //Number of Static Variables
	public static final String STSHN ="STSHN"; //Shannon Information Content
	public static final String STTDE ="STTDE"; //COCOMO Embedded Total Months
	public static final String STTDO ="STTDO"; //COCOMO Organic Total Months
	public static final String STTDS ="STTDS"; //COCOMO Semi-detached Total Months
	//public static final String STTLN ="STTLN"; //Total Preprocessed Source Lines
	//public static final String STTOT ="STTOT"; //Total Number of Tokens
	//public static final String STTPP ="STTPP"; //Total Unpreprocessed Source Lines
	//public static final String STVAR ="STVAR"; //Number of Identifiers
	public static final String STVOL ="STVOL"; //Program Volume
	public static final String STZIP ="STZIP"; //Zipf Prediction of STTOT

	
	
	
	
	//Project Wide Metrics
	public static final String STNRA ="STNRA"; //Number of Recursions Across Project
	public static final String STNEA ="STNEA"; //Number of Entry Points Across Project
	public static final String STNFA ="STNFA"; //Number of Functions Across Project
	public static final String STCYA ="STCYA"; //Cyclomatic Complexity Across Project
	
	//Metrics used in Sonar
	
	public static final String PATH ="PATH";		// replaces STPTH
	public static final String GOTO ="GOTO";		// replaces STGTO
	public static final String COMPLEXITY ="COMPLEXITY";	// replaces STCYC
	public static final String CALLING ="CALLING";	// replaces STM29
	public static final String CALLS ="CALLS";		// replaces STCAL
	public static final String PARAM ="PARAM";		// replaces STPAR
	public static final String STMT ="STMT";		// replaces STXLN and STST3
	public static final String LEVEL ="LEVEL";		// replaces STMIF
	public static final String RETURN ="RETURN";	// replaces STM19
	public static final String WMC ="WMC";			// replaces STWMC
	public static final String lcom4 ="lcom4";			// replaces STLCM
	public static final String CBO ="CBO";			// replaces STCBO
	public static final String RFC ="RFC";			// replaces STRFC
	public static final String DIT ="DIT";			// replaces STDIT
	public static final String NOP ="NOP";			// replaces STNOP
	public static final String VOCF ="VOCF";		// COMPOUND1
	public static final String COMF ="COMF";		// COMPOUND2

}
