
--Prints Hello World, then prints the even numbers between 1 and 100.
--For The record, I'm not totally certain about the last line, how it works
--exactly. I havent gotten to that part of the tutorial yet.
--But for ~one day of reading I think it's decent.
main = do
	putStrLn "Hello World"
	let evens = map show (takeWhile (<100) (map (*2) [1..]))
	putStrLn $ show evens