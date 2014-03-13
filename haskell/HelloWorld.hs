import Data.List (tails)
import System.Random
--Prints Hello World, then prints the odd & even numbers between 0 and 100.
--And does some other things too, as I learn Haskell
main = do
	putStrLn "Hello World"

	--This commented section prints the list of odd/even numbers less than 100
	--	but the printed list is a list of strings of said numbers
	--let evens = map show (takeWhile (<100) (map (*2) [0..]))
	--let odds = map show (takeWhile (<100) (map (\x -> 2*x + 1) [0..]))
	--putStrLn $ show odds
	--putStrLn $ show evens

	--This version is slightly more readable, and returns a list of Num
	let odds1 = findNumbersLessThanFittingFunction 100 (\x -> 2*x + 1)
	let evens1 = findNumbersLessThanFittingFunction 100 (\x -> 2*x)
	putStrLn $ show odds1
	putStrLn $ show evens1
	putStrLn $ show . removeDuplicates $ [0..10] ++ [0..10]

	--seed the random number generator to 0, arbitrarily, then get it in rand.
	setStdGen (mkStdGen 0)
	rand <- getStdGen
	-- all three of these return the same sequence of numbers, b/c FP :/
	print $ head $ take 1 (randoms rand :: [Double]) 
	--print $ take 1 (randoms rand :: [Double]) 
	--print $ take 1 (randoms rand :: [Double]) 
	-- so yea, I can't generate a totally random number on each and every call
	--in pure FP. I could theoretically make some wrappers to keep spliting the
	--rand on each invocation, but not tonight. Tomorrow likely.


	


findNumbersLessThanFittingFunction::(Num a, Ord a, Enum a) => a->(a->a)->[a]
findNumbersLessThanFittingFunction limit fn = 
	let functionedElems = map fn [0 ..];
		underLimit = takeWhile (<limit) functionedElems;
	in underLimit --underLimit is the return list

--Simple implementation of removing the duplicate elements from a list
--	could also be done with calling "nub" after including
--	import Data.List (nub)
removeDuplicates::(Eq a) => [a] -> [a]
removeDuplicates xs = 
	foldr (\x acc -> if (x `elem` acc) then acc else x:acc) [] xs

--I'm copying this from the tutorial because it's really interesting IMO
--Searches for a specific list within a list., and returns true if it's there
search :: (Eq a) => [a] -> [a] -> Bool  
search needle haystack =   
	let nlen = length needle  
	in  foldl (\acc x -> if take nlen x == needle then True else acc) False (tails haystack) 


--I was going to study OS by writing this psuedo VA translator,
	--but then the types were being far too complicated.
--translates a Virtual Address using a LPT
--size of physical mem, size of page, size of virt mem, [ptbr],[[pagetable]] [va] -> 
translateVirtualAddress::Int->Int->Int->[Int]->[[Int]]->[Int] -> [Int]
translateVirtualAddress physicalMem pageSize virtMem ptbr pageTable va = 
	vpn--placeholder
	where 	offsetSize =  logBase 2 (fromIntegral pageSize :: Float);
			virtAddrSize = logBase 2 (fromIntegral virtMem :: Float);
			physAddrSize = logBase 2 (fromIntegral physicalMem :: Float);
			vpnSize = virtAddrSize - offsetSize;
			--this translation is stupid. I'm giving up. There are better ways
			vpn = take (read ( show vpnSize ):: Int) va;




