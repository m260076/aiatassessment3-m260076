# Base: Diffblue's official CProver image, which ships jbmc (and cbmc)
# already built and on PATH — see https://github.com/diffblue/cbmc/releases
# ("We are also releasing new versions as images in Dockerhub ... The
# CProver tools are present in the $PATH of the container.")
#
# NOTE: pin/check this tag against the current latest release before
# using in a live offering — https://github.com/diffblue/cbmc/releases
FROM diffblue/cbmc:6.10.0

# Install a JDK (to compile the student app) and Python/pip (for Semgrep),
# plus git for basic repo operations inside the Codespace.
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        openjdk-21-jdk-headless \
        python3 \
        python3-pip \
        git \
        curl \
    && rm -rf /var/lib/apt/lists/*

# Install Semgrep.
RUN pip3 install --no-cache-dir --break-system-packages semgrep || \
    pip3 install --no-cache-dir semgrep

WORKDIR /workspace
