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
	--setStdGen (mkStdGen 0)
	--rand <- getStdGen
	let rand = mkStdGen 0
	-- all three of these return the same sequence of numbers, b/c FP :/
	let randNum = head $ take 1 (randoms rand :: [Double]) 
	print $ randNum
	rand <- newStdGen
	let randNum2 = getRand rand
	rand <- newStdGen
	print$ randNum2
	let randNum2 = getRand rand
	rand <- newStdGen
	print$ randNum2

	--Well, it appears there is not really a great way to get reliable random
	--numbers out of Haskell - although I have demonsrated each iteration of
	--this code does give you 3 random numbers, it's just a bit too much work
	--and not straightforward, even in the slightest.
	--I really love the curried/partial functions idea,
	--and the general "syntax" is neat, but its overall a bit too difficult to
	--realize what's actually happening currently.
	--Probably SUPER awesome for math work, but I'm doing none of that 
	--currently. 
	--Like, FP is cool, but as with many modern languages, I feel like I want
	--more syntax to describe what's happening.

	--But good to have learned this much. Not very useful, but neat to know
	-- (like Russian :P)

	--let nextRand = 0.0
	--putStrLn "first rand call"
	--let (nextRand, newrand) = random rand :: (Double, StdGen)
	--rand <- fst $ split newrand
	--putStrLn "after rand call"
	----putStrLn $ ("Rand 0: " ++ show nextRand)
	--putStrLn $ show nextRand
	--let (nextRand2, rand) = random rand :: (Double, StdGen)
	--print $ "Rand 1: " ++ show nextRand2
	--rand <- rand'
	--print $ show nextRand
	--print $ take 1 (randoms rand :: [Double]) 
	--print $ take 1 (randoms rand :: [Double]) 
	-- so yea, I can't generate a totally random number on each and every call
	--in pure FP. I could theoretically make some wrappers to keep spliting the
	--rand on each invocation, but not tonight. Tomorrow likely.


	print $ binarySum [1, 0, 0, 1]
	print $ binarySum [1,0,0,1,1,1,0,1,0,1,0,1]

	--print $ finiteRandoms 10 rand 
	

getRand::StdGen->Double
getRand rand = head $ take 1 (randoms rand :: [Double]) 



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


binarySum::(Num a, Eq a)=>[a]->a
binarySum num = foldl (\acc x -> (acc*2)+(if x == 0 then 0 else 1) ) 0 num

--From the Learn You A Haskell tutorial (although this I actually found on SO)
finiteRandoms :: (RandomGen g, Random a, Num n, Eq n) => n -> g -> ([a], g)  
finiteRandoms 0 gen = ([], gen)  
finiteRandoms x gen =   
    let (value, newGen) = random gen  
        (restOfList, finalGen) = finiteRandoms (x-1) newGen  
    in  (value:restOfList, finalGen)
