import com.xeiam.xchange.*
import com.xeiam.xchange.service.streaming.*
import com.xeiam.xchange.currency.*

import com.xeiam.xchange.bitstamp.*
import com.xeiam.xchange.bitstamp.service.streaming.*;

import com.xeiam.xchange.coinfloor.*
import com.xeiam.xchange.coinfloor.dto.streaming.*
import com.xeiam.xchange.coinfloor.streaming.*
import com.xeiam.xchange.coinfloor.dto.streaming.marketdata.*

import com.xeiam.xchange.btcchina.*
import com.xeiam.xchange.examples.btcchina.*
import com.xeiam.xchange.btcchina.service.streaming.*

StreamingExchangeService b ;

assert args.length > 0, "specify exchange, for example com.xeiam.xchange.coinfloor.CoinfloorExchange"

if (args[0] == "btcchina") {
	b = BTCChinaExamplesUtils.getExchange().getStreamingExchangeService(new BTCChinaStreamingConfiguration(true, true, CurrencyPair.BTC_CNY, CurrencyPair.LTC_CNY, CurrencyPair.LTC_BTC))
} else if (args[0] == "bitstamp") {
	b = ExchangeFactory.INSTANCE.createExchange(new BitstampExchange().getDefaultExchangeSpecification()).getStreamingExchangeService(new BitstampStreamingConfiguration())
} else {
	b = ExchangeFactory.INSTANCE.createExchange(args[0]).getStreamingExchangeService()
}

b.connect()

executorService = java.util.concurrent.Executors.newSingleThreadExecutor()
java.util.concurrent.Future<?> eventCatcherThread = executorService.submit({
	while (true) {
		exchangeEvent = b.getNextEvent()
		if (exchangeEvent.getEventType() == ExchangeEventType.TICKER) println exchangeEvent.getPayload()
	}
})

if (!["btcchina", "bitstamp"].contains(args[0])) { // coinfloor
	println b.watchTicker("BTC", "GBP").getPayload().get("generic");
	b.watchOrders("BTC", "GBP").getPayload().get("generic");
}

// the thread waits here until the Runnable is done.
eventCatcherThread.get();
executorService.shutdown();
b.disconnect();
