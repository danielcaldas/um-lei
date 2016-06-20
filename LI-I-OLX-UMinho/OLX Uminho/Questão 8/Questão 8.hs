
import Data.Char

type House = ([Auction],[Auction])
type Auction = (Int,String,String,String,Int,Int,String)

--fun��o que nos diz quanto � que uma dado item valorizou, isto se o leil�o para esse item foi encerrado
--iv -> initial value
--fv -> final value

valoriza :: House -> [Int]
valoriza (_,[]) = []
valoriza (rs,((_,_,_,_,iv,fv,_):fs)) = if fv>iv then (fv-iv):valoriza (rs,fs)
                                                else valoriza (rs,fs)
												   
--fun��o que calcula a m�dia de uma lista de inteiros												   
media :: [Int] -> Int
media l = sum l `div` length l

medVal :: House -> Int
medVal (_,[]) = 0
medVal h = media (valoriza h)