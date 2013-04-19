package services.pub;
import java.io.*; 
public class ChineseMoney {

	
	private String number[]= 

	{"","壹","贰","叁","肆","伍","陆","柒","捌","玖"}; 
	private String unit[]={"","拾","佰","仟"}; 
	private String small[]={"角","分"}; 
	//private String strNumber,strUnit,strAll; 

	//是否在number中 
	private boolean IsInNumber(String strNumber) 
	{ 
	boolean inNumber=false; 
	for (int i=0;i<9;i++) 
	{ 
	if (strNumber.compareTo (number[i])==0) inNumber=true; 
	} 
	return inNumber; 
	} 


	private String SplitChineseNumber(int intUnit,String strInt) 
	{ 
	int l=strInt.length (); 
	int j,k,zeorCountTemp=0; 
	String strUnit="",strNumber="",strAll=""; 

	//判断在千万到万位 是否全为0，是的话，不返回“万”，返回“”； 
	boolean temp=false; 
	for (k=0;k<l;k++) 
	{ 
	String strTemp=strInt.substring(k,k+1); 
	int intTemp=Integer.parseInt(strTemp); 

	if (intTemp!=0) temp=true; 
	} 
	if (temp==false) 
	{ 
	if (intUnit==5)return ""; 
	} 


	int checkK=0; 
	//正式开始转换 
	for (k=0;k<l;k++) 
	{ 
	String strTemp=strInt.substring(k,k+1); 
	int intTemp=Integer.parseInt(strTemp); 
	strNumber= number[intTemp]; 

	//j 从 
	j=l-1-k; 

	strUnit=unit[j]; 


	//数值＋单位 
	//如果数值＝0，数值＝“” 
	if (intTemp==0) 
	{ 
	// 
	if (zeorCountTemp==0) 
	{ 
	//单位＝零 
	strUnit=strUnit.replace('拾','零'); 
	strUnit=strUnit.replace('佰','零'); 
	strUnit=strUnit.replace('仟','零'); 
	} 
	else 
	{ 
	//多零情况下，单位＝“” 
	strUnit=strUnit.replaceAll("拾",""); 
	strUnit=strUnit.replaceAll("佰",""); 
	strUnit=strUnit.replaceAll("仟",""); 
	} 
	zeorCountTemp++; 
	} 
	checkK=k; 
	strAll+=strNumber+strUnit; 
	} 

	return strAll; 
	} 

	private String onlyInt(int intInt) 
	{ 
	String strInt; 
	strInt=String.valueOf(intInt); 
	int l=strInt.length(); 

	String strAll=""; //按照四位 一分隔 来计算 
	if (l>8)//亿 


	{ 
	strAll+=this.SplitChineseNumber(9,strInt.substring(0,l-8))+"亿"; 
	strAll+=this.SplitChineseNumber(5,strInt.substring(l-8,l-4))+"万"; 
	strAll+=this.SplitChineseNumber(1,strInt.substring(l-4,l))+"元"; 
	} 
	else if (l>4)//万 { 
	{ strAll+=this.SplitChineseNumber(5,strInt.substring(0,l-4))+"万"; 
	strAll+=this.SplitChineseNumber(1,strInt.substring(l-4,l))+"元"; 

	} 
	else if (l>0) 
	{ 
	strAll+=this.SplitChineseNumber(1,strInt)+"元"; 
	}// 
	// 
	// 
	// 
	// 100101000 
	int checkL=strAll.length(); 

	char strTemp2; 
	for (int k=1;k<checkL;k++) 
	{ 
	strTemp2=strAll.charAt(k); 
	if (strTemp2=='零') 
	{ 
	//判断零的前后是否有数字，无数字则删除这个零 
	String strBeforeTemp=strAll.substring(k-1,k); 
	String strAfterTemp=strAll.substring(k+1,k+2); 
	if (!this.IsInNumber(strBeforeTemp)&&!this.IsInNumber 

	(strAfterTemp)) 
	{ 
	strBeforeTemp=strAll.substring(0,k); 
	strAfterTemp=strAll.substring(k+1,checkL); 
	strAll= strBeforeTemp+strAfterTemp; 
	break; 
	} 

	} 
	} 

	return strAll; 

	} 

	private String onlySmall(int intSmall) 
	{ 
	  String strNumber,strUnit,strAll; 
	  strNumber="";strUnit="";strAll=""; 
	  String strSmall,strTemp; 
	  strSmall=String.valueOf(intSmall); 
	  int i; 
	  if (intSmall>=10) 
	  { 
	    for (i=0;i<strSmall.length();i++) 
	    { 
	    strTemp=String.valueOf(intSmall).substring(i,i+1); 
	      if (Integer.parseInt(strTemp)!=0) 
	      { 
	      strNumber=number[Integer.parseInt(strTemp)]; 
	      strUnit=small[i]; 
	      strAll+=strNumber+strUnit; 
	      } 
	    } 
	  } 
	  else 
	  { 
	    if (intSmall!=0) 
	    { 
	    strNumber=number[intSmall]; 
	    strUnit=small[1]; 
	    strAll+=strNumber+strUnit; 
	    } 
	  } 

	return strAll; 
	} 

	public String getChineseMoney(double number) 
	{ 
	//四舍五入 
	number=(number*100+0.5)/100; 

	String strAll,strChineseInt,strChineseSmall,strZheng;; 
	int intInt,intSmall; 
	strChineseInt="";strChineseSmall="";strZheng=""; 

	//整数部分 
	intInt=(int)( number*100/100); 
	if (intInt!=0) 
	{ 
	strChineseInt=onlyInt(intInt); 
	} 
	//小数部分 
	double temp=(number-intInt)*100*100/100; 
	//对小数部分四舍五入 
	intSmall=(int)(temp*100+0.5)/100; 
	if (intSmall!=0) 
	{ 
	strChineseSmall=onlySmall(intSmall); 
	} 
	else 
	{ 
	strZheng="整"; 
	} 
	strAll=strChineseInt+strChineseSmall+strZheng; 
	return strAll; 
	} 
	public static void main(String args[]) throws IOException 
	{ 
	ChineseMoney cm=new ChineseMoney(); 
	double money; 
	String strMoney,strChineseMoney; 
	strMoney=""; 
	//读取 
	System.out.println("输入货币(四舍五入):"); 
	BufferedReader cin = new BufferedReader(new InputStreamReader 

	(System.in)); 
	strMoney = cin.readLine(); 
	money=Double.parseDouble(strMoney); 
	//money=12346.465;//Double.parseDouble(strMoney); 
	strChineseMoney=cm.getChineseMoney(money); 
	System.out.println(strChineseMoney); 
	} 
}
