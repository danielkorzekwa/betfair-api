package dk.bot.betfairservice.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BFMarketTradedVolumeFactoryTest {

	@Test
	public void testCreate() {
		
	String tradedVolumeString = ":3671548~0~0.0~0.0~0.0|9.8~6.0|10.0~1085.06|10.17~46.46|10.5~10603.26|10.66~1163.74|11.0~2873.18|11.14~2618.24|11.5~1040.7|11.63~481.52|12.0~2438.64:2155906~0~0.0~0.0~0.0|18.0~6.0|18.5~30.0|19.5~4.38|20.0~12.0|21.0~836.72|22.0~1066.6|23.0~1537.72|24.0~1964.5|24.23~4.0|25.0~1608.78|26.0~180.12|27.0~98.66|28.0~112.98|28.1~194.9|29.0~9.08|29.07~484.84|30.0~175.52|31.01~352.76|32.0~180.1|32.95~390.52|34.0~33.86|34.88~246.52|36.0~4.04|36.82~156.5|38.76~338.66|40.7~68.8|42.64~70.0|44.57~58.0|46.51~94.0|48.45~48.0|53.3~6.0|62.99~44.0|92.06~6.0:1026928~0~0.0~0.0~0.0|77.52~4.0|82.37~40.0|85.0~6.0|87.21~102.4|90.0~10.0|92.06~140.2|95.0~38.76|96.9~115.88|100.0~57.4|106.59~17.82|110.0~189.84|120.0~274.14|130.0~184.48|140.0~197.02|150.0~53.84:1513720~0~0.0~0.0~0.0|53.3~10.0|58.14~109.38|62.99~249.4|65.0~221.9|67.83~107.28|70.0~991.6|72.68~21.52|75.0~739.98|80.0~42.2|92.06~4.0:1024660~0~0.0~0.0~0.0|48.45~4.0|50.0~327.4|53.3~88.2|55.0~1037.92|58.14~51.36|60.0~222.48|62.99~20.22|65.0~104.82|67.83~48.26|70.0~61.82|72.68~161.82|75.0~144.8|77.52~52.5|80.0~84.74:2618925~0~0.0~0.0~0.0|34.0~6.0|34.88~36.46|36.0~41.68|36.82~62.08|38.0~138.76|38.76~132.62|40.0~114.56|40.7~218.28|42.0~281.48|42.64~236.9|44.0~755.7|44.57~140.2|46.0~674.68|46.51~94.24|48.0~82.06|48.45~10.32|50.0~519.08|55.0~228.2:1291180~0~0.0~0.0~0.0|4.17~20.0|4.85~366.3|4.94~840.2|5.0~69.44|5.04~1179.34|5.1~3449.66|5.14~2323.8|5.2~9014.38|5.23~4158.46|5.3~9589.7|5.33~6008.8|5.4~6374.5|5.43~1337.08|5.5~15190.5|5.6~6557.82|5.7~29.76:1411758~0~0.0~0.0~0.0|6.78~178.26|6.98~1748.24|7.0~3774.34|7.17~2220.2|7.2~6806.64|7.36~1923.56|7.4~3941.56|7.56~2026.18|7.6~6022.32|7.75~16.62|7.8~778.44:2353569~0~0.0~0.0~0.0|6.98~8.0|7.56~6.0|7.75~34.0|7.95~118.38|8.14~347.74|8.33~373.7|8.53~160.0|8.72~70.0|9.0~86.02|9.11~77.64|9.2~2719.94|9.3~485.58|9.4~2610.56|9.5~301.4|9.6~3007.76|9.69~821.3|9.8~2895.76|10.0~1259.88|10.17~424.88|10.66~162.18:627204~0~0.0~0.0~0.0|32.0~234.12|32.95~64.46|34.0~1774.22|34.88~55.54|36.0~1360.04|36.82~138.92|38.0~787.46|38.76~457.28|40.0~122.98|40.7~268.44|42.0~2.08|42.64~35.18|44.57~18.3:1451625~0~0.0~0.0~0.0|21.0~445.68|21.32~27.02|22.0~559.78|23.0~2226.7|24.0~735.92|25.0~228.56|25.19~27.5|26.0~665.68|26.16~99.34|27.0~386.14|27.13~409.78|28.0~500.12|28.1~397.74|29.0~79.6|29.07~242.96|31.01~393.94:1118035~0~0.0~0.0~0.0|82.37~4.0|92.06~2.32|96.9~1.68|100.0~32.34|106.59~1.34|110.0~13.08|116.28~43.6|120.0~26.58|125.97~116.1|130.0~99.18|135.66~28.88|140.0~86.62|145.35~12.68|150.0~251.86|155.04~12.5|160.0~168.28|170.0~37.86|174.42~0.28:2177453~0~0.0~0.0~0.0|34.0~98.04|36.0~447.7|38.0~1510.48|38.76~218.06|40.0~645.18|40.7~59.78|42.0~109.94|42.64~120.06|44.0~200.26|44.57~105.78|46.0~305.56|46.51~167.92|48.0~214.52|48.45~146.56|53.3~3.34:598829~0~0.0~0.0~0.0|34.0~10.0|34.88~13.48|36.82~174.18|38.0~115.8|38.76~178.44|40.0~103.22|40.7~240.42|42.0~503.34|42.64~145.1|44.0~703.88|44.57~95.3|46.0~753.24|48.0~433.04|48.45~7.66|50.0~46.74|53.3~16.5:2324698~0~0.0~0.0~0.0|13.57~2.08|14.05~3.92|17.44~3.46|17.93~38.54|18.41~4.0|18.9~123.8|19.0~35.62|19.38~148.3|19.5~1727.26|20.0~2682.12|20.35~378.8|21.0~1570.2|21.32~309.04|22.0~265.2|22.29~120.76|23.0~374.74|23.26~103.4|24.0~60.76|24.23~73.52:2111026~0~0.0~0.0~0.0|32.0~21.58|34.0~2.52|40.0~58.0|42.0~28.4|44.0~62.72|46.0~22.6|48.0~33.12|50.0~151.14|55.0~283.8|58.14~22.0|60.0~398.36|62.99~123.48|65.0~288.94|67.83~62.0|70.0~293.18|72.68~60.88|75.0~370.54|77.52~38.62|80.0~5.84|82.37~27.24:722826~0~0.0~0.0~0.0|15.0~87.02|15.5~1114.7|16.0~1090.64|16.47~24.0|16.5~439.82|16.96~560.7|17.0~1122.82|17.5~717.48|17.93~135.12|18.0~271.74|18.41~267.26|18.5~266.54|18.9~932.7|19.0~280.6|19.38~476.38|19.5~361.22|20.0~417.14|20.35~89.42|21.0~1323.34|22.0~858.26|23.0~488.06|24.0~30.8|25.0~60.12|26.0~10.0:1289030~0~0.0~0.0~0.0|17.44~32.48|17.93~15.0|18.0~286.0|18.41~98.0|18.5~846.0|18.9~192.64|19.0~95.42|19.38~1080.08|19.5~1003.98|20.0~3766.92|20.35~645.02|21.0~897.06|21.32~46.66|22.0~481.2|22.29~88.94|23.0~283.2:2674715~0~0.0~0.0~0.0|8.53~2.02|8.72~3.98|9.0~456.04|9.11~305.18|9.2~4416.7|9.3~1991.92|9.4~6574.3|9.5~942.02|9.6~1949.66|9.69~23.7|9.8~391.46";
		
	BFMarketTradedVolume marketTradedVolume = BFMarketTradedVolumeFactory.create(35,tradedVolumeString);
	
	assertEquals(35, marketTradedVolume.getMarketId());
	assertEquals(19, marketTradedVolume.getRunnerTradedVolume().size());
	
	assertEquals(3671548,marketTradedVolume.getRunnerTradedVolume().get(0).getSelectionId());
	assertEquals(10,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().size());
	
	assertEquals(9.8,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(0).getPrice(),0);
	assertEquals(6,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(0).getTradedVolume(),0);
	assertEquals(10.0,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(1).getPrice(),0);
	assertEquals(1085.06,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(1).getTradedVolume(),0);
	assertEquals(10.17,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(2).getPrice(),0);
	assertEquals(46.46,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(2).getTradedVolume(),0);
	assertEquals(10.5,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(3).getPrice(),0);
	assertEquals(10603.26,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(3).getTradedVolume(),0);
	assertEquals(12,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(9).getPrice(),0);
	assertEquals(2438.64,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().get(9).getTradedVolume(),0);
	
	assertEquals(2674715,marketTradedVolume.getRunnerTradedVolume().get(18).getSelectionId());
	assertEquals(11,marketTradedVolume.getRunnerTradedVolume().get(18).getPriceTradedVolume().size());
	
	assertEquals(8.53,marketTradedVolume.getRunnerTradedVolume().get(18).getPriceTradedVolume().get(0).getPrice(),0);
	assertEquals(2.02,marketTradedVolume.getRunnerTradedVolume().get(18).getPriceTradedVolume().get(0).getTradedVolume(),0);
	assertEquals(8.72,marketTradedVolume.getRunnerTradedVolume().get(18).getPriceTradedVolume().get(1).getPrice(),0);
	assertEquals(3.98,marketTradedVolume.getRunnerTradedVolume().get(18).getPriceTradedVolume().get(1).getTradedVolume(),0);
	assertEquals(9.69,marketTradedVolume.getRunnerTradedVolume().get(18).getPriceTradedVolume().get(9).getPrice(),0);
	assertEquals(23.7,marketTradedVolume.getRunnerTradedVolume().get(18).getPriceTradedVolume().get(9).getTradedVolume(),0);
	
	}
	
	@Test
	public void testCreateNoMatchedAtAll() {
		String tradedVolumeString =":30246~0~0.0~0.0~0.0:30247~0~0.0~0.0~0.0";
		BFMarketTradedVolume marketTradedVolume = BFMarketTradedVolumeFactory.create(22,tradedVolumeString);
		
		assertEquals(22, marketTradedVolume.getMarketId());
		assertEquals(2, marketTradedVolume.getRunnerTradedVolume().size());
		
		assertEquals(30246,marketTradedVolume.getRunnerTradedVolume().get(0).getSelectionId());
		assertEquals(0,marketTradedVolume.getRunnerTradedVolume().get(0).getPriceTradedVolume().size());
		
		assertEquals(30247,marketTradedVolume.getRunnerTradedVolume().get(1).getSelectionId());
		assertEquals(0,marketTradedVolume.getRunnerTradedVolume().get(1).getPriceTradedVolume().size());
	}

}
