//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//###           14 Sep 06  -- Keltin Leung-- ReduceListener support, eliminate underflow report in error recovery
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 11 "Parser.y"
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
//#line 25 "Parser.java"
interface ReduceListener {
  public boolean onReduce(String rule);
}




public class Parser
             extends BaseParser
             implements ReduceListener
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

ReduceListener reduceListener = null;
void yyclearin ()       {yychar = (-1);}
void yyerrok ()         {yyerrflag=0;}
void addReduceListener(ReduceListener l) {
  reduceListener = l;}


//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short BOOL=258;
public final static short INT=259;
public final static short STRING=260;
public final static short CLASS=261;
public final static short NULL=262;
public final static short EXTENDS=263;
public final static short THIS=264;
public final static short WHILE=265;
public final static short FOR=266;
public final static short IF=267;
public final static short ELSE=268;
public final static short RETURN=269;
public final static short BREAK=270;
public final static short NEW=271;
public final static short FI=272;
public final static short DO=273;
public final static short OD=274;
public final static short PRINT=275;
public final static short READ_INTEGER=276;
public final static short READ_LINE=277;
public final static short LITERAL=278;
public final static short IDENTIFIER=279;
public final static short AND=280;
public final static short OR=281;
public final static short STATIC=282;
public final static short INSTANCEOF=283;
public final static short NUMINSTANCES=284;
public final static short GUARD_SEPERATOR=285;
public final static short INC=286;
public final static short DEC=287;
public final static short LESS_EQUAL=288;
public final static short GREATER_EQUAL=289;
public final static short EQUAL=290;
public final static short NOT_EQUAL=291;
public final static short UMINUS=292;
public final static short EMPTY=293;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    2,    6,    6,    7,    7,    7,    9,    9,   10,
   10,    8,    8,   11,   12,   12,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   14,   14,   14,   25,
   25,   22,   22,   24,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   27,   27,   26,   26,
   28,   28,   17,   18,   21,   15,   29,   29,   16,   16,
   30,   30,   19,   19,   20,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    2,
    3,    6,    2,    0,    2,    2,    0,    1,    0,    3,
    1,    7,    6,    3,    2,    0,    1,    2,    1,    1,
    1,    1,    2,    2,    2,    1,    3,    1,    0,    2,
    0,    2,    4,    5,    1,    1,    1,    5,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    2,    2,    2,    2,    2,    2,    3,    3,
    1,    4,    5,    6,    4,    5,    1,    1,    1,    0,
    3,    1,    5,    9,    1,    6,    2,    0,    3,    3,
    5,    3,    2,    1,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   13,   17,
    0,    7,    8,    6,    9,    0,    0,   12,   15,    0,
    0,   16,   10,    0,    4,    0,    0,    0,    0,   11,
    0,   21,    0,    0,    0,    0,    5,    0,    0,    0,
   26,   23,   20,   22,    0,   78,   71,    0,    0,    0,
    0,   85,    0,    0,    0,    0,    0,   77,    0,    0,
    0,    0,    0,    0,    0,   24,   27,   36,   25,    0,
   29,   30,   31,   32,    0,    0,    0,    0,    0,    0,
    0,   47,    0,    0,    0,   45,    0,   46,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   28,   33,   34,   35,    0,    0,
    0,   65,   66,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   40,    0,    0,    0,    0,    0,
    0,    0,   89,    0,    0,    0,   90,    0,    0,   69,
   70,    0,    0,    0,   62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   92,    0,   72,    0,    0,
   95,    0,   75,    0,   43,    0,    0,    0,   83,    0,
    0,    0,   73,    0,    0,   76,    0,   44,    0,    0,
   86,   91,   74,    0,   87,    0,   84,
};
final static short yydgoto[] = {                          2,
    3,    4,   67,   20,   33,    8,   11,   22,   34,   35,
   68,   45,   69,   70,   71,   72,   73,   74,   75,   76,
   77,   86,   79,   88,   81,  177,   82,  139,  191,   89,
};
final static short yysindex[] = {                      -241,
 -245,    0, -241,    0, -228,    0, -238,  -75,    0,    0,
  -74,    0,    0,    0,    0, -229, -155,    0,    0,    1,
  -88,    0,    0,  -87,    0,   17,  -30,   18, -155,    0,
 -155,    0,  -85,   27,   21,   32,    0,  -47, -155,  -47,
    0,    0,    0,    0,   -2,    0,    0,   37,   46, 1001,
 1018,    0,  -91, 1018,   47,   48,   51,    0,   53,   54,
 1018, 1018, 1018, 1018,  488,    0,    0,    0,    0,   36,
    0,    0,    0,    0,   50,   52,   60,   59,  800,    0,
 -172,    0, 1018, 1018,  488,    0,  156,    0, -255,  800,
   72,   35, -261, 1018,   89,   94, 1018, -147,  -35,  -35,
  -38,  -38, -136,  293,    0,    0,    0,    0, 1018, 1018,
 1018,    0,    0, 1018, 1018, 1018, 1018, 1018, 1018, 1018,
 1018, 1018, 1018, 1018,    0, 1018, 1018,  105,  305,   88,
  378,   29,    0, 1018,  107,  821,    0,  800,  -19,    0,
    0,  411,  115,  116,    0,  800,  980,  968,   71,   71,
 1079, 1079,   24,   24,  -38,  -38,  -38,   71,   71,  433,
  445, 1018,   29, 1018, 1061,    0,  467,    0,  497, 1018,
    0, -121,    0, 1018,    0, 1018,  130,  128,    0,  509,
  -95,   29,    0,  800,  134,    0,  956,    0, 1018,   29,
    0,    0,    0,  138,    0,   29,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  189,    0,   67,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  136,    0,    0,  155,    0,
  155,    0,    0,    0,  159,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -58,    0,    0,    0,    0,  -82,
  -57,    0,    0,  -82,    0,    0,    0,    0,    0,    0,
  -82,  -82,  -82,  -82,  -82,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  944,    0,  118,
    0,    0,  -82,  -58,  -82,    0,    0,    0,    0,  145,
    0,    0,    0,  -82,    0,    0,  -82,    0,  560,  584,
  764,  839,    0,    0,    0,    0,    0,    0,  -82,  -82,
  -82,    0,    0,  -82,  -82,  -82,  -82,  -82,  -82,  -82,
  -82,  -82,  -82,  -82,    0,  -82,  -82,   38,    0,    0,
    0,  -58,    0,  -82,    0,  -82,    0,   -5,    0,    0,
    0,    0,    0,    0,    0,  -32,   81,   -4,   83,  359,
  457,  613,  776, 1105,  863,  892,  916,  597, 1073,    0,
    0,  -18,  -58,  -82,   91,    0,    0,    0,    0,  -82,
    0,    0,    0,  -82,    0,  -82,    0,  164,    0,    0,
  -33,  -58,    0,    5,    0,    0,  -26,    0,  -13,  -58,
    0,    0,    0,    0,    0,  -58,    0,
};
final static short yygindex[] = {                         0,
    0,  203,  196,  -10,   -1,    0,    0,    0,  179,    0,
    4,    0, -118,  -79,    0,    0,    0,    0,    0,    0,
    0, 1027, 1338, 1208,    0,    0,    0,   49,    0,  158,
};
final static int YYTABLESIZE=1514;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         88,
   39,   94,   27,   27,  130,   27,   88,  125,   37,   21,
  125,   88,  137,  166,   48,   24,  133,   48,   32,    1,
   32,  171,   80,  134,  170,   88,   37,   39,   43,  134,
   64,   48,   48,    5,    7,   82,   61,   65,   82,   61,
    9,   42,   63,   44,  179,   81,  181,   10,   81,   23,
   18,   92,  126,   61,   61,  126,   29,   31,   61,   25,
  122,   64,   30,  192,   39,  120,   48,   38,   65,  125,
  121,  195,   40,   63,   42,   41,   83,  197,   42,   42,
   42,   42,   42,   42,   42,   84,   94,   95,   61,   88,
   96,   88,   97,   98,  105,   42,   42,   42,   42,   42,
   42,   12,   13,   14,   15,   16,  128,  122,  106,  194,
  107,  135,  120,  118,  126,  119,  125,  121,  108,  109,
   41,   60,   66,   58,   60,  136,   58,   62,   42,  140,
   42,  143,   62,   62,  141,   62,   62,   62,   60,   60,
   58,   58,  144,   60,  162,   58,  164,  168,   62,   39,
   62,   41,   62,   62,   46,  173,  174,  185,   38,   46,
   46,  126,   46,   46,   46,   12,   13,   14,   15,   16,
  188,  170,  190,   60,  193,   58,   38,   46,  196,   46,
   46,   62,   12,   13,   14,   15,   16,   91,    1,   14,
   26,   28,  122,   37,    5,   19,   41,  120,  118,   18,
  119,  125,  121,   93,   79,    6,   19,   17,   46,   36,
  178,   93,    0,  132,    0,  124,    0,  123,  127,    0,
   41,   41,    0,   88,   88,   88,   88,   88,   88,    0,
   88,   88,   88,   88,    0,   88,   88,   88,   88,   88,
   88,   88,   88,   88,   88,   88,  126,  112,  113,   88,
   88,   88,   88,   88,   12,   13,   14,   15,   16,   46,
   41,   47,   48,   49,   50,   41,   51,   52,   53,    0,
   54,    0,   55,   56,   57,   58,   61,    0,    0,    0,
   59,   60,    0,   61,   62,   12,   13,   14,   15,   16,
   46,    0,   47,   48,   49,   50,    0,   51,   52,   53,
    0,   54,    0,   55,   56,   57,   58,    0,    0,  112,
  113,   59,   60,    0,   61,   62,    0,   42,   42,    0,
    0,    0,    0,   42,   42,   42,   42,   42,   42,  122,
    0,    0,    0,  145,  120,  118,    0,  119,  125,  121,
    0,  122,    0,    0,    0,  163,  120,  118,    0,  119,
  125,  121,  124,    0,  123,  127,  112,  113,    0,    0,
   60,   60,   58,   58,  124,    0,  123,  127,    0,   41,
   62,   62,   58,   58,    0,    0,   62,   62,   62,   62,
   62,   62,    0,  126,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  126,    0,   46,   46,   59,
    0,    0,   59,   46,   46,   46,   46,   46,   46,    0,
    0,    0,    0,    0,  122,    0,   59,   59,  165,  120,
  118,   59,  119,  125,  121,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  110,  111,  124,    0,  123,
  127,  112,  113,  114,  115,  116,  117,  122,    0,    0,
    0,   59,  120,  118,  172,  119,  125,  121,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  126,  122,
  124,    0,  123,  127,  120,  118,    0,  119,  125,  121,
    0,  122,    0,    0,    0,    0,  120,  118,    0,  119,
  125,  121,  124,    0,  123,  127,    0,   54,    0,    0,
   54,  126,  176,  122,  124,    0,  123,  127,  120,  118,
    0,  119,  125,  121,   54,   54,    0,    0,    0,   54,
   64,    0,    0,  126,  182,  175,  124,   65,  123,  127,
    0,    0,   63,  122,    0,  126,    0,    0,  120,  118,
    0,  119,  125,  121,    0,  122,    0,    0,    0,   54,
  120,  118,    0,  119,  125,  121,  124,  126,  123,  127,
    0,    0,    0,    0,    0,    0,    0,  189,  124,    0,
  123,  127,  110,  111,    0,    0,    0,    0,  112,  113,
  114,  115,  116,  117,  110,  111,    0,  126,    0,  183,
  112,  113,  114,  115,  116,  117,   63,    0,    0,  126,
   63,   63,   63,   63,   63,    0,   63,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   63,   63,   63,
   64,   63,   63,    0,   64,   64,   64,   64,   64,    0,
   64,    0,    0,    0,    0,    0,    0,   57,   59,   59,
   57,   64,   64,   64,    0,   64,   64,    0,   59,   59,
    0,    0,   63,   55,   57,   57,   55,  110,  111,   57,
    0,    0,    0,  112,  113,  114,  115,  116,  117,    0,
   55,   55,    0,    0,    0,   55,   64,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   57,
  110,  111,    0,    0,    0,    0,  112,  113,  114,  115,
  116,  117,    0,    0,    0,   55,    0,    0,    0,    0,
    0,    0,  110,  111,    0,    0,    0,    0,  112,  113,
  114,  115,  116,  117,  110,  111,    0,    0,    0,    0,
  112,  113,  114,  115,  116,  117,   54,   54,    0,    0,
    0,    0,    0,    0,    0,    0,  110,  111,  103,   46,
    0,   47,  112,  113,  114,  115,  116,  117,   53,    0,
    0,    0,    0,   56,   57,   58,    0,    0,    0,    0,
   59,   60,    0,   61,   62,    0,  110,  111,    0,    0,
    0,    0,  112,  113,  114,  115,  116,  117,  110,  111,
    0,    0,    0,    0,  112,  113,  114,  115,  116,  117,
   67,    0,    0,    0,   67,   67,   67,   67,   67,    0,
   67,    0,    0,    0,    0,    0,   49,    0,   49,   49,
   49,   67,   67,   67,    0,   67,   67,    0,    0,    0,
    0,    0,    0,   49,   49,   49,  122,   49,   49,   63,
   63,  120,  118,    0,  119,  125,  121,   63,   63,   63,
   63,    0,    0,   64,    0,    0,   67,    0,    0,  124,
   65,  123,  127,   64,   64,   63,    0,    0,   49,    0,
    0,   64,   64,   64,   64,   68,   57,   57,    0,   68,
   68,   68,   68,   68,    0,   68,   57,   57,    0,    0,
  126,    0,   55,   55,    0,    0,   68,   68,   68,   51,
   68,   68,    0,   51,   51,   51,   51,   51,    0,   51,
    0,    0,    0,   30,    0,    0,    0,    0,    0,    0,
   51,   51,   51,    0,   51,   51,    0,    0,   52,    0,
    0,   68,   52,   52,   52,   52,   52,    0,   52,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   52,
   52,   52,   53,   52,   52,   51,   53,   53,   53,   53,
   53,    0,   53,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   53,   53,   53,    0,   53,   53,    0,
   45,    0,    0,    0,   52,   45,   45,    0,   45,   45,
   45,    0,  122,    0,    0,    0,    0,  120,  118,    0,
  119,  125,  121,   45,  122,   45,   45,    0,   53,  120,
  118,    0,  119,  125,  121,  124,  122,  123,    0,    0,
    0,  120,  118,    0,  119,  125,  121,  124,    0,  123,
    0,    0,    0,   64,   45,    0,    0,    0,    0,  124,
   85,  123,    0,   67,   67,   63,  126,    0,    0,    0,
   64,   67,   67,   67,   67,   49,   49,   65,  126,    0,
    0,    0,   63,   49,   49,   49,   49,    0,    0,    0,
  126,   78,    0,    0,    0,    0,    0,    0,    0,  110,
  111,    0,   46,    0,   47,  112,  113,  114,  115,  116,
  117,   53,    0,   64,    0,    0,   56,   57,   58,    0,
   65,    0,    0,   59,   60,    0,   61,   62,    0,    0,
   78,    0,    0,   56,    0,  122,   56,    0,   68,   68,
  120,  118,    0,  119,  125,  121,   68,   68,   68,   68,
   56,   56,    0,    0,    0,   56,    0,    0,  124,    0,
  123,    0,   51,   51,    0,   50,    0,   50,   50,   50,
   51,   51,   51,   51,    0,    0,    0,    0,   78,    0,
    0,    0,   50,   50,   50,   56,   50,   50,    0,  126,
    0,   52,   52,    0,    0,    0,    0,    0,    0,   52,
   52,   52,   52,   41,    0,    0,    0,    0,    0,   78,
    0,   78,    0,    0,    0,   53,   53,   50,    0,    0,
    0,    0,    0,   53,   53,   53,   53,    0,   78,    0,
    0,    0,    0,    0,    0,   78,   78,    0,    0,    0,
    0,    0,   78,   45,   45,    0,    0,    0,    0,   45,
   45,   45,   45,   45,   45,  110,  111,    0,    0,    0,
    0,  112,  113,  114,  115,  116,  117,  110,    0,    0,
    0,    0,   80,  112,  113,  114,  115,  116,  117,    0,
    0,    0,   46,    0,   47,  112,  113,  114,  115,  116,
  117,   53,    0,    0,    0,    0,   56,   57,   58,   46,
    0,   47,    0,   59,   60,    0,   61,   62,   53,    0,
    0,   80,    0,   56,   57,   58,    0,    0,    0,    0,
   59,   60,    0,   61,   62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   12,   13,   14,
   15,   16,   46,    0,   47,   48,   49,   50,    0,   51,
   52,   53,    0,   54,    0,   55,   56,   57,   58,   80,
    0,    0,    0,   59,   60,    0,    0,    0,    0,    0,
    0,    0,   56,   56,    0,    0,    0,    0,    0,    0,
    0,    0,   56,   56,  112,  113,  114,  115,    0,    0,
   80,    0,   80,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   50,   50,    0,   87,   90,   80,
    0,   87,   50,   50,   50,   50,   80,   80,   99,  100,
  101,  102,  104,   80,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  129,    0,  131,    0,    0,    0,    0,    0,    0,    0,
    0,  138,    0,    0,  142,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  146,  147,  148,    0,
    0,  149,  150,  151,  152,  153,  154,  155,  156,  157,
  158,  159,    0,  160,  161,    0,    0,    0,    0,    0,
    0,  167,    0,  169,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  138,
    0,  180,    0,    0,    0,    0,    0,  184,    0,    0,
    0,  186,    0,  187,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   59,   59,   91,   91,   84,   91,   40,   46,   41,   11,
   46,   45,  274,  132,   41,   17,  272,   44,   29,  261,
   31,   41,   41,  285,   44,   59,   59,   41,   39,  285,
   33,   58,   59,  279,  263,   41,   41,   40,   44,   44,
  279,   38,   45,   40,  163,   41,  165,  123,   44,  279,
  125,   53,   91,   58,   59,   91,   40,   40,   63,   59,
   37,   33,   93,  182,   44,   42,   93,   41,   40,   46,
   47,  190,   41,   45,   37,  123,   40,  196,   41,   42,
   43,   44,   45,   46,   47,   40,   40,   40,   93,  123,
   40,  125,   40,   40,   59,   58,   59,   60,   61,   62,
   63,  257,  258,  259,  260,  261,  279,   37,   59,  189,
   59,   40,   42,   43,   91,   45,   46,   47,   59,   61,
  123,   41,  125,   41,   44,   91,   44,   37,   91,   41,
   93,  279,   42,   43,   41,   45,   46,   47,   58,   59,
   58,   59,  279,   63,   40,   63,   59,   41,   58,   59,
   60,  123,   62,   63,   37,   41,   41,  279,   41,   42,
   43,   91,   45,   46,   47,  257,  258,  259,  260,  261,
   41,   44,  268,   93,   41,   93,   59,   60,   41,   62,
   63,   91,  257,  258,  259,  260,  261,  279,    0,  123,
  279,  279,   37,  279,   59,   41,  279,   42,   43,   41,
   45,   46,   47,   59,   41,    3,   11,  282,   91,   31,
  162,   54,   -1,   58,   -1,   60,   -1,   62,   63,   -1,
  279,  279,   -1,  257,  258,  259,  260,  261,  262,   -1,
  264,  265,  266,  267,   -1,  269,  270,  271,  272,  273,
  274,  275,  276,  277,  278,  279,   91,  286,  287,  283,
  284,  285,  286,  287,  257,  258,  259,  260,  261,  262,
  279,  264,  265,  266,  267,  279,  269,  270,  271,   -1,
  273,   -1,  275,  276,  277,  278,  281,   -1,   -1,   -1,
  283,  284,   -1,  286,  287,  257,  258,  259,  260,  261,
  262,   -1,  264,  265,  266,  267,   -1,  269,  270,  271,
   -1,  273,   -1,  275,  276,  277,  278,   -1,   -1,  286,
  287,  283,  284,   -1,  286,  287,   -1,  280,  281,   -1,
   -1,   -1,   -1,  286,  287,  288,  289,  290,  291,   37,
   -1,   -1,   -1,   41,   42,   43,   -1,   45,   46,   47,
   -1,   37,   -1,   -1,   -1,   41,   42,   43,   -1,   45,
   46,   47,   60,   -1,   62,   63,  286,  287,   -1,   -1,
  280,  281,  280,  281,   60,   -1,   62,   63,   -1,  279,
  280,  281,  290,  291,   -1,   -1,  286,  287,  288,  289,
  290,  291,   -1,   91,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   91,   -1,  280,  281,   41,
   -1,   -1,   44,  286,  287,  288,  289,  290,  291,   -1,
   -1,   -1,   -1,   -1,   37,   -1,   58,   59,   41,   42,
   43,   63,   45,   46,   47,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  280,  281,   60,   -1,   62,
   63,  286,  287,  288,  289,  290,  291,   37,   -1,   -1,
   -1,   93,   42,   43,   44,   45,   46,   47,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,   37,
   60,   -1,   62,   63,   42,   43,   -1,   45,   46,   47,
   -1,   37,   -1,   -1,   -1,   -1,   42,   43,   -1,   45,
   46,   47,   60,   -1,   62,   63,   -1,   41,   -1,   -1,
   44,   91,   58,   37,   60,   -1,   62,   63,   42,   43,
   -1,   45,   46,   47,   58,   59,   -1,   -1,   -1,   63,
   33,   -1,   -1,   91,   58,   93,   60,   40,   62,   63,
   -1,   -1,   45,   37,   -1,   91,   -1,   -1,   42,   43,
   -1,   45,   46,   47,   -1,   37,   -1,   -1,   -1,   93,
   42,   43,   -1,   45,   46,   47,   60,   91,   62,   63,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   59,   60,   -1,
   62,   63,  280,  281,   -1,   -1,   -1,   -1,  286,  287,
  288,  289,  290,  291,  280,  281,   -1,   91,   -1,   93,
  286,  287,  288,  289,  290,  291,   37,   -1,   -1,   91,
   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   58,   59,   60,
   37,   62,   63,   -1,   41,   42,   43,   44,   45,   -1,
   47,   -1,   -1,   -1,   -1,   -1,   -1,   41,  280,  281,
   44,   58,   59,   60,   -1,   62,   63,   -1,  290,  291,
   -1,   -1,   93,   41,   58,   59,   44,  280,  281,   63,
   -1,   -1,   -1,  286,  287,  288,  289,  290,  291,   -1,
   58,   59,   -1,   -1,   -1,   63,   93,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   93,
  280,  281,   -1,   -1,   -1,   -1,  286,  287,  288,  289,
  290,  291,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,  280,  281,   -1,   -1,   -1,   -1,  286,  287,
  288,  289,  290,  291,  280,  281,   -1,   -1,   -1,   -1,
  286,  287,  288,  289,  290,  291,  280,  281,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  280,  281,  261,  262,
   -1,  264,  286,  287,  288,  289,  290,  291,  271,   -1,
   -1,   -1,   -1,  276,  277,  278,   -1,   -1,   -1,   -1,
  283,  284,   -1,  286,  287,   -1,  280,  281,   -1,   -1,
   -1,   -1,  286,  287,  288,  289,  290,  291,  280,  281,
   -1,   -1,   -1,   -1,  286,  287,  288,  289,  290,  291,
   37,   -1,   -1,   -1,   41,   42,   43,   44,   45,   -1,
   47,   -1,   -1,   -1,   -1,   -1,   41,   -1,   43,   44,
   45,   58,   59,   60,   -1,   62,   63,   -1,   -1,   -1,
   -1,   -1,   -1,   58,   59,   60,   37,   62,   63,  280,
  281,   42,   43,   -1,   45,   46,   47,  288,  289,  290,
  291,   -1,   -1,   33,   -1,   -1,   93,   -1,   -1,   60,
   40,   62,   63,  280,  281,   45,   -1,   -1,   93,   -1,
   -1,  288,  289,  290,  291,   37,  280,  281,   -1,   41,
   42,   43,   44,   45,   -1,   47,  290,  291,   -1,   -1,
   91,   -1,  280,  281,   -1,   -1,   58,   59,   60,   37,
   62,   63,   -1,   41,   42,   43,   44,   45,   -1,   47,
   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,
   58,   59,   60,   -1,   62,   63,   -1,   -1,   37,   -1,
   -1,   93,   41,   42,   43,   44,   45,   -1,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   58,
   59,   60,   37,   62,   63,   93,   41,   42,   43,   44,
   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   58,   59,   60,   -1,   62,   63,   -1,
   37,   -1,   -1,   -1,   93,   42,   43,   -1,   45,   46,
   47,   -1,   37,   -1,   -1,   -1,   -1,   42,   43,   -1,
   45,   46,   47,   60,   37,   62,   63,   -1,   93,   42,
   43,   -1,   45,   46,   47,   60,   37,   62,   -1,   -1,
   -1,   42,   43,   -1,   45,   46,   47,   60,   -1,   62,
   -1,   -1,   -1,   33,   91,   -1,   -1,   -1,   -1,   60,
   40,   62,   -1,  280,  281,   45,   91,   -1,   -1,   -1,
   33,  288,  289,  290,  291,  280,  281,   40,   91,   -1,
   -1,   -1,   45,  288,  289,  290,  291,   -1,   -1,   -1,
   91,   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  280,
  281,   -1,  262,   -1,  264,  286,  287,  288,  289,  290,
  291,  271,   -1,   33,   -1,   -1,  276,  277,  278,   -1,
   40,   -1,   -1,  283,  284,   -1,  286,  287,   -1,   -1,
   84,   -1,   -1,   41,   -1,   37,   44,   -1,  280,  281,
   42,   43,   -1,   45,   46,   47,  288,  289,  290,  291,
   58,   59,   -1,   -1,   -1,   63,   -1,   -1,   60,   -1,
   62,   -1,  280,  281,   -1,   41,   -1,   43,   44,   45,
  288,  289,  290,  291,   -1,   -1,   -1,   -1,  132,   -1,
   -1,   -1,   58,   59,   60,   93,   62,   63,   -1,   91,
   -1,  280,  281,   -1,   -1,   -1,   -1,   -1,   -1,  288,
  289,  290,  291,  123,   -1,   -1,   -1,   -1,   -1,  163,
   -1,  165,   -1,   -1,   -1,  280,  281,   93,   -1,   -1,
   -1,   -1,   -1,  288,  289,  290,  291,   -1,  182,   -1,
   -1,   -1,   -1,   -1,   -1,  189,  190,   -1,   -1,   -1,
   -1,   -1,  196,  280,  281,   -1,   -1,   -1,   -1,  286,
  287,  288,  289,  290,  291,  280,  281,   -1,   -1,   -1,
   -1,  286,  287,  288,  289,  290,  291,  280,   -1,   -1,
   -1,   -1,   45,  286,  287,  288,  289,  290,  291,   -1,
   -1,   -1,  262,   -1,  264,  286,  287,  288,  289,  290,
  291,  271,   -1,   -1,   -1,   -1,  276,  277,  278,  262,
   -1,  264,   -1,  283,  284,   -1,  286,  287,  271,   -1,
   -1,   84,   -1,  276,  277,  278,   -1,   -1,   -1,   -1,
  283,  284,   -1,  286,  287,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,   -1,  264,  265,  266,  267,   -1,  269,
  270,  271,   -1,  273,   -1,  275,  276,  277,  278,  132,
   -1,   -1,   -1,  283,  284,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  280,  281,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  290,  291,  286,  287,  288,  289,   -1,   -1,
  163,   -1,  165,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  280,  281,   -1,   50,   51,  182,
   -1,   54,  288,  289,  290,  291,  189,  190,   61,   62,
   63,   64,   65,  196,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   83,   -1,   85,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   94,   -1,   -1,   97,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  109,  110,  111,   -1,
   -1,  114,  115,  116,  117,  118,  119,  120,  121,  122,
  123,  124,   -1,  126,  127,   -1,   -1,   -1,   -1,   -1,
   -1,  134,   -1,  136,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  162,
   -1,  164,   -1,   -1,   -1,   -1,   -1,  170,   -1,   -1,
   -1,  174,   -1,  176,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=293;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'","'?'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","BOOL","INT","STRING",
"CLASS","NULL","EXTENDS","THIS","WHILE","FOR","IF","ELSE","RETURN","BREAK",
"NEW","FI","DO","OD","PRINT","READ_INTEGER","READ_LINE","LITERAL","IDENTIFIER",
"AND","OR","STATIC","INSTANCEOF","NUMINSTANCES","GUARD_SEPERATOR","INC","DEC",
"LESS_EQUAL","GREATER_EQUAL","EQUAL","NOT_EQUAL","UMINUS","EMPTY",
};
final static String yyrule[] = {
"$accept : Program",
"Program : ClassList",
"ClassList : ClassList ClassDef",
"ClassList : ClassDef",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : VOID",
"Type : BOOL",
"Type : STRING",
"Type : CLASS IDENTIFIER",
"Type : Type '[' ']'",
"ClassDef : CLASS IDENTIFIER ExtendsClause '{' FieldList '}'",
"ExtendsClause : EXTENDS IDENTIFIER",
"ExtendsClause :",
"FieldList : FieldList VariableDef",
"FieldList : FieldList FunctionDef",
"FieldList :",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
"FunctionDef : STATIC Type IDENTIFIER '(' Formals ')' StmtBlock",
"FunctionDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : VariableDef",
"Stmt : SimpleStmt ';'",
"Stmt : IfStmt",
"Stmt : GuardStmt",
"Stmt : WhileStmt",
"Stmt : ForStmt",
"Stmt : ReturnStmt ';'",
"Stmt : PrintStmt ';'",
"Stmt : BreakStmt ';'",
"Stmt : StmtBlock",
"SimpleStmt : LValue '=' Expr",
"SimpleStmt : Call",
"SimpleStmt :",
"Receiver : Expr '.'",
"Receiver :",
"LValue : Receiver IDENTIFIER",
"LValue : Expr '[' Expr ']'",
"Call : Receiver IDENTIFIER '(' Actuals ')'",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : Expr '?' Expr ':' Expr",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr '<' Expr",
"Expr : Expr '>' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '(' Expr ')'",
"Expr : INC Expr",
"Expr : DEC Expr",
"Expr : Expr INC",
"Expr : Expr DEC",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Expr : READ_INTEGER '(' ')'",
"Expr : READ_LINE '(' ')'",
"Expr : THIS",
"Expr : NEW IDENTIFIER '(' ')'",
"Expr : NEW Type '[' Expr ']'",
"Expr : INSTANCEOF '(' Expr ',' IDENTIFIER ')'",
"Expr : NUMINSTANCES '(' IDENTIFIER ')'",
"Expr : '(' CLASS IDENTIFIER ')' Expr",
"Constant : LITERAL",
"Constant : NULL",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"GuardStmt : IF GuardClauses FI",
"GuardStmt : DO GuardClauses OD",
"GuardClauses : GuardClauses GUARD_SEPERATOR Expr ':' Stmt",
"GuardClauses : Expr ':' Stmt",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
};

//#line 474 "Parser.y"
    
	/**
	 * 打印当前归约所用的语法规则<br>
	 * 请勿修改。
	 */
    public boolean onReduce(String rule) {
		if (rule.startsWith("$$"))
			return false;
		else
			rule = rule.replaceAll(" \\$\\$\\d+", "");

   	    if (rule.endsWith(":"))
    	    System.out.println(rule + " <empty>");
   	    else
			System.out.println(rule);
		return false;
    }
    
    public void diagnose() {
		addReduceListener(this);
		yyparse();
	}
//#line 706 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0 || valptr<0)   //check for under & overflow here
            {
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0 || valptr<0)   //check for under & overflow here
              {
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    if (reduceListener == null || reduceListener.onReduce(yyrule[yyn])) // if intercepted!
      switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 56 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 62 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 66 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 76 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 82 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 86 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 90 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 9:
//#line 94 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 10:
//#line 98 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 11:
//#line 102 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 12:
//#line 108 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 13:
//#line 114 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 14:
//#line 118 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 15:
//#line 124 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 16:
//#line 128 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 17:
//#line 132 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 19:
//#line 140 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 20:
//#line 147 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 21:
//#line 151 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 22:
//#line 158 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 23:
//#line 162 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 168 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 25:
//#line 174 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 26:
//#line 178 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 27:
//#line 185 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 28:
//#line 190 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 37:
//#line 206 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 38:
//#line 210 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 39:
//#line 214 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 41:
//#line 221 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 42:
//#line 227 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 43:
//#line 234 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 44:
//#line 240 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 45:
//#line 249 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 48:
//#line 255 "Parser.y"
{
                        yyval.expr = new Tree.Ternary(Tree.TERNARY_COND, val_peek(4).expr, val_peek(2).expr, val_peek(0).expr, val_peek(3).loc);
                    }
break;
case 49:
//#line 259 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 50:
//#line 263 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 51:
//#line 267 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 52:
//#line 271 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 53:
//#line 275 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 54:
//#line 279 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 283 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 287 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 291 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 295 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 299 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 303 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 61:
//#line 307 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 311 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 63:
//#line 315 "Parser.y"
{
                        yyval.expr = new Tree.Unary(Tree.PREINC, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 64:
//#line 319 "Parser.y"
{
                        yyval.expr = new Tree.Unary(Tree.PREDEC, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 65:
//#line 323 "Parser.y"
{
                        yyval.expr = new Tree.Unary(Tree.POSTINC, val_peek(1).expr, val_peek(1).loc);
                    }
break;
case 66:
//#line 327 "Parser.y"
{
                        yyval.expr = new Tree.Unary(Tree.POSTDEC, val_peek(1).expr, val_peek(1).loc);
                    }
break;
case 67:
//#line 331 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 68:
//#line 335 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 69:
//#line 339 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 70:
//#line 343 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 71:
//#line 347 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 72:
//#line 351 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 73:
//#line 355 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 74:
//#line 359 "Parser.y"
{
                		yyval.expr = new Tree.TypeTest(val_peek(3).expr, val_peek(1).ident, val_peek(5).loc);
                	}
break;
case 75:
//#line 363 "Parser.y"
{
                        yyval.expr = new Tree.NumInstances(val_peek(1).ident, val_peek(3).loc);
                    }
break;
case 76:
//#line 367 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 77:
//#line 373 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 78:
//#line 377 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 80:
//#line 384 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 81:
//#line 391 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 82:
//#line 395 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 83:
//#line 402 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 84:
//#line 408 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 85:
//#line 414 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 86:
//#line 420 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 87:
//#line 426 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 88:
//#line 430 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 89:
//#line 436 "Parser.y"
{
                        yyval.stmt = new Tree.Guard(Tree.Guard.Type.Condition, val_peek(1).guard_clauses, val_peek(2).loc);
                    }
break;
case 90:
//#line 441 "Parser.y"
{
                        yyval.stmt = new Tree.Guard(Tree.Guard.Type.Loop, val_peek(1).guard_clauses, val_peek(2).loc);
                    }
break;
case 91:
//#line 447 "Parser.y"
{
                        yyval.guard_clauses.add(new Tree.GuardClause(val_peek(2).expr, val_peek(0).stmt, val_peek(3).loc));
                    }
break;
case 92:
//#line 451 "Parser.y"
{
                        yyval.guard_clauses = new ArrayList<Tree.GuardClause>();
                    	yyval.guard_clauses.add(new Tree.GuardClause(val_peek(2).expr, val_peek(0).stmt, val_peek(1).loc));
                    }
break;
case 93:
//#line 458 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 94:
//#line 462 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 95:
//#line 468 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
//#line 1354 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
