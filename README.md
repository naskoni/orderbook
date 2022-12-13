# ORDERBOOK

The app connects on startup to the Kraken Websockets API, subscribes to the XBT/USD and ETH/USD pairs, and starts printing reports
every 10 seconds (configurable).

The design of the application allows the functionality to be easily extended: for each desired pair, we must inherit
AbstractProcessor and add only @Component("{pair}"). Format of each pair is "A/B", where A and B are ISO 4217-A3 for standardized
assets and popular unique symbol if not standardized. We also need to add the desired pair to the subscription property in
application.properties.